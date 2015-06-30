package projectmp.client.lighting;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.block.Block.BlockFaces;
import projectmp.common.world.TimeOfDay;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class LightingEngine {

	public static final float TRANSITION_MULTIPLIER_SECS = 2.5f;
	/**
	 * The shadow base colour (default black)
	 */
	public static final Color SHADOW_BASE_COLOR = new Color(0, 0, 0, 1);

	// the essentials
	private World world;
	private Main main;
	private int sizex = 1;
	private int sizey = 1;

	// ToD and transitions
	public float lastDayBrightness = TimeOfDay.DAYTIME.lightLevel / 127f;
	/**
	 * This is the time of day colour with transitions
	 */
	public Color timeOfDayColor = new Color(TimeOfDay.DAYTIME.color);
	
	// reused color objects
	private Color mixingColor0 = new Color();
	private Color mixingColor1 = new Color();
	private Color skyLightTransition = new Color();
	
	// all the light data
	private byte[][] skyLighting;
	private LightTiles lightData;
	
	// the lighting renderer
	private LightingRenderer lightingRenderer;

	// everything related to lighting updates
	private int lastUpdateLengthNano = 0;
	private boolean isUpdateScheduled = false;
	private float lastUpdateCamX = 0;
	private float lastUpdateCamY = 0;
	private final Pool<LightingUpdate> lightingUpdatePool = Pools.get(LightingUpdate.class, 256);
	private Array<LightingUpdate> lightingUpdates = new Array<LightingUpdate>(256);

	/**
	 * used for debug
	 */
	private int lightingUpdateMethodCalls = 0;

	public LightingEngine(World world) {
		this.world = world;
		main = this.world.main;
		sizex = world.sizex;
		sizey = world.sizey;

		lightingRenderer = new LightingRenderer(this);
		
		skyLighting = new byte[sizex][sizey];
		lightData = new LightTiles(sizex, sizey);
	}

	/**
	 * call NOT between batch begin/end
	 */
	public void render(WorldRenderer renderer, SpriteBatch batch) {
		float magicDeltaAssist = Gdx.graphics.getDeltaTime() / TRANSITION_MULTIPLIER_SECS;
		TimeOfDay currentTOD = world.time.getCurrentTimeOfDay();
		Color.rgb888ToColor(skyLightTransition, currentTOD.color);

		lastDayBrightness += ((currentTOD.lightLevel / 127f) - lastDayBrightness)
				* magicDeltaAssist;
		timeOfDayColor.r += ((skyLightTransition).r - timeOfDayColor.r) * magicDeltaAssist;
		timeOfDayColor.g += ((skyLightTransition).g - timeOfDayColor.g) * magicDeltaAssist;
		timeOfDayColor.b += ((skyLightTransition).b - timeOfDayColor.b) * magicDeltaAssist;

		if (Math.abs((renderer.camera.camerax + (Settings.DEFAULT_WIDTH / 2f)) - lastUpdateCamX) > Settings.DEFAULT_WIDTH
				- (World.tilesizex * 2)) {
			scheduleLightingUpdate();
		}
		if (Math.abs((renderer.camera.cameray + (Settings.DEFAULT_HEIGHT / 2f)) - lastUpdateCamY) > Settings.DEFAULT_HEIGHT
				- (World.tilesizey * 2)) {
			scheduleLightingUpdate();
		}

		if (isUpdateScheduled) {
			int prex = (int) MathUtils
					.clamp(((renderer.camera.camerax / World.tilesizex) - (Settings.DEFAULT_WIDTH / World.tilesizex)),
							0f, world.sizex);
			int prey = (int) MathUtils
					.clamp(((renderer.camera.cameray / World.tilesizey) - (Settings.DEFAULT_HEIGHT / World.tilesizey)),
							0f, world.sizey);
			int postx = (int) MathUtils.clamp((renderer.camera.camerax / World.tilesizex)
					+ ((Settings.DEFAULT_WIDTH / World.tilesizex) * 2), 0f, world.sizex);
			int posty = (int) MathUtils.clamp((renderer.camera.cameray / World.tilesizey)
					+ ((Settings.DEFAULT_HEIGHT / World.tilesizey) * 2), 0f, world.sizey);

			lastUpdateCamX = renderer.camera.camerax + (Settings.DEFAULT_WIDTH / 2f);
			lastUpdateCamY = renderer.camera.cameray + (Settings.DEFAULT_HEIGHT / 2f);

			updateLighting(prex, prey, postx, posty);
			isUpdateScheduled = false;
		}

		lightingRenderer.render(renderer, batch);
	}

	public void updateLighting(int prex, int prey, int postx, int posty) {
		long nano = System.nanoTime();

		// reset the lighting and calculate the sky lights
		resetLightingAndCalcSky(prex, prey, postx, posty);

		// do all the lighting updates
		doLightingUpdates(prex, prey, postx, posty);

		// free the lighting updates
		while (lightingUpdates.size > 0) {
			LightingUpdate l = lightingUpdates.pop();
			lightingUpdatePool.free(l);
		}

		lastUpdateLengthNano = (int) (System.nanoTime() - nano);
	}

	public void resetLightingAndCalcSky(int originx, int originy, int width, int height) {
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);

		for (int x = originx; x < width; x++) {
			for (int y = originy; y < height; y++) {
				// reset all
				skyLighting[x][y] = 0;
				setBrightness((byte) 0, x, y);
				setLightColor(Color.rgb888(0, 0, 0), x, y);
			}

			int y = 0;
			boolean terminate = false;
			while (!terminate) {
				if (((world.getBlock(x, y).isSolid(world, x, y) & BlockFaces.UP) == BlockFaces.UP) || y >= sizey) {
					terminate = true;

					// scatter the light at the bottom
					recursiveSky(x, y, (byte) 127, true);
					
					break;
				}

				// set as sky lighting
				skyLighting[x][y] = 127;
				y++;
			}

		}
	}

	public void doLightingUpdates(int originx, int originy, int width, int height) {
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);

		lightingUpdateMethodCalls = 0;

		for (int i = lightingUpdates.size - 1; i >= 0; i--) {
			LightingUpdate l = lightingUpdates.get(i);
			setBrightness(l.brightness, l.x, l.y);
			mixColors(l.x, l.y, l.color);

			recursiveLight(l.x, l.y, l.brightness, l.color, true);
		}
	}
	
	private void recursiveSky(int x, int y, byte bright, boolean source){
		if (bright <= 0) {
			return;
		}
		if (skyLighting[x][y] >= bright && !source) {
			return;
		}
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) {
			return;
		}

		skyLighting[x][y] = bright;

		bright = (byte) MathUtils.clamp(bright
				- (world.getBlock(x, y).lightSubtraction(world, x, y) * 127), 0, 127);

		if (bright <= 0) return;

		if ((x - 1 >= 0) && !(getBrightness(x - 1, y) >= bright)) {
			recursiveSky(x - 1, y, bright, false);
		}
		if ((y - 1 >= 0) && !(getBrightness(x, y - 1) >= bright)) {
			recursiveSky(x, y - 1, bright, false);
		}
		if ((x + 1 < sizex) && !(getBrightness(x + 1, y) >= bright)) {
			recursiveSky(x + 1, y, bright, false);
		}
		if ((y + 1 < sizey) && !(getBrightness(x, y + 1) >= bright)) {
			recursiveSky(x, y + 1, bright, false);
		}
	}

	private void recursiveLight(int x, int y, byte bright, int color, boolean source) {
		lightingUpdateMethodCalls++;

		// brightness <= 0, return
		// brightness >= bright and not a source, return
		// out of bounds, return
		
		// set block brightness to bright
		
		// subtract bright from block's opacity
		// if bright <= 0 return
		// mix neighbours colours
		// recursion
		
		if (bright <= 0) {
			return;
		}
		if (getBrightness(x, y) >= bright && !source) {
			return;
		}
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) {
			return;
		}

		setBrightness(bright, x, y);

		bright = (byte) MathUtils.clamp(bright
				- (world.getBlock(x, y).lightSubtraction(world, x, y) * 127), 0, 127);

		if (bright <= 0) return;

		mixColors(x - 1, y, color);
		mixColors(x, y - 1, color);
		mixColors(x, y + 1, color);
		mixColors(x + 1, y, color);

		if ((x - 1 >= 0) && !(getBrightness(x - 1, y) >= bright)) {
			recursiveLight(x - 1, y, bright, color, false);
		}
		if ((y - 1 >= 0) && !(getBrightness(x, y - 1) >= bright)) {
			recursiveLight(x, y - 1, bright, color, false);
		}
		if ((x + 1 < sizex) && !(getBrightness(x + 1, y) >= bright)) {
			recursiveLight(x + 1, y, bright, color, false);
		}
		if ((y + 1 < sizey) && !(getBrightness(x, y + 1) >= bright)) {
			recursiveLight(x, y + 1, bright, color, false);
		}
	}

	private void mixColors(int x, int y, int color) {
		Color.rgb888ToColor(mixingColor0, color);
		Color.rgb888ToColor(mixingColor1, getLightColor(x, y));

		Color parameter = mixingColor0;
		Color atPosition = mixingColor1;

		atPosition.lerp(parameter, 0.5f);
		setLightColor(Color.rgb888(atPosition), x, y);
		color = Color.rgb888(atPosition);
	}

	public void setLightSource(byte bright, int color, int x, int y) {
		lightingUpdates.add(lightingUpdatePool.obtain().set(x, y, bright, color));
		scheduleLightingUpdate();
	}

	public void setBrightness(byte l, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		
		lightData.brightness[x][y] = l;
	}

	public void setLightColor(int color, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		
		lightData.color[x][y] = color;
	}

	public byte getBrightness(int posx, int posy) {
		int x = posx;
		int y = posy;
		if (x < 0) x = 0;
		if (x >= sizex) x = sizex - 1;
		if (y < 0) y = 0;
		if (y >= sizey) y = sizey - 1;

		return lightData.brightness[x][y];
	}

	public int getLightColor(int posx, int posy) {
		int x = posx;
		int y = posy;
		if (x < 0) x = 0;
		if (x >= sizex) x = sizex - 1;
		if (y < 0) y = 0;
		if (y >= sizey) y = sizey - 1;

		return lightData.color[x][y];
	}
	
	public byte getSkyLight(int posx, int posy){
		int x = posx;
		int y = posy;
		if (x < 0) x = 0;
		if (x >= sizex) x = sizex - 1;
		if (y < 0) y = 0;
		if (y >= sizey) y = sizey - 1;
		
		return skyLighting[x][y];
	}
	
	/**
	 * accounts for current time of day light with transition
	 * @param posx
	 * @param posy
	 * @return float but clamped to 0-127
	 */
	public float getSkyLightFromTOD(byte light){
		return  MathUtils.clamp(light * lastDayBrightness, 0f, 127f);
	}
	
	public float getActualLighting(int posx, int posy){
		int x = posx;
		int y = posy;
		if (x < 0) x = 0;
		if (x >= sizex) x = sizex - 1;
		if (y < 0) y = 0;
		if (y >= sizey) y = sizey - 1;
		
		byte brightness = getBrightness(x, y);
		float sky = getSkyLightFromTOD(getSkyLight(x, y));
		
		if(sky > brightness){
			return MathUtils.clamp(sky, 0f, 127f);
		}else{
			return brightness;
		}
	}
	
	public LightingRenderer getRenderer(){
		return lightingRenderer;
	}

	// update related things below
	
	public int getLastUpdateLength() {
		return lastUpdateLengthNano;
	}

	public void scheduleLightingUpdate() {
		if (world.isServer) return;
		isUpdateScheduled = true;
	}

	public boolean isLightingUpdateScheduled() {
		return isUpdateScheduled;
	}

}
