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

	private World world;
	private Main main;
	private int sizex = 1;
	private int sizey = 1;

	private byte[][] brightness;
	private byte[][] tempBrightness;
	/**
	 * rgb888
	 */
	private int[][] lightColor;
	private int[][] tempLightColor;
	
	private boolean[][] isPartOfSky;

	private TimeOfDay lastTOD = TimeOfDay.DAYTIME;
	public float lastDayBrightness = TimeOfDay.DAYTIME.lightLevel / 127f;
	private Color daylightColor = new Color(0, 0, 0, 1);
	private final Color shadowColor = new Color(0, 0, 0, 1);
	private Color tempColor = new Color();
	private Color tempColor2 = new Color();
	private Color tempColor3 = new Color();
	private Color tempColor4 = new Color();
	private Color tempColor5 = new Color();
	private Color tempColor6 = new Color();
	private Color tempColor7 = new Color();

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

		brightness = new byte[sizex][sizey];
		tempBrightness = new byte[sizex][sizey];
		lightColor = new int[sizex][sizey];
		tempLightColor = new int[sizex][sizey];
		isPartOfSky = new boolean[sizex][sizey];

		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				lightColor[x][y] = Color.rgb888(0, 0, 0);
			}
		}

		copyToTemp();
	}

	/**
	 * call NOT between batch begin/end
	 */
	public void render(WorldRenderer renderer, SpriteBatch batch) {
		if(lastTOD != world.time.getCurrentTimeOfDay()) scheduleLightingUpdate();
		lastTOD = world.time.getCurrentTimeOfDay();

		if((int) (lastDayBrightness * 127) != world.time.getCurrentTimeOfDay().lightLevel){
			byte byteform = (byte) (lastDayBrightness * 127);
			
			lastDayBrightness += ((world.time.getCurrentTimeOfDay().lightLevel / 127f) - lastDayBrightness)
					* Gdx.graphics.getDeltaTime() * 2.0f;
			
			Color.rgb888ToColor(tempColor, world.time.getCurrentTimeOfDay().color);
			daylightColor.lerp(tempColor, Gdx.graphics.getDeltaTime() * 2.0f);
			
//			if(Math.abs((world.worldTime.getCurrentTimeOfDay().lightLevel - ((int) (lastDayBrightness * 127)))) <= 1){
//				// forcefully set the colour and brightness when it's "close enough"
//				lastDayBrightness = (world.worldTime.getCurrentTimeOfDay().lightLevel / 127f);
//				Color.rgb888ToColor(daylightColor, world.worldTime.getCurrentTimeOfDay().color);
//			}
			
			if((byte) (lastDayBrightness * 127) != byteform) scheduleLightingUpdate();
		}
		
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

		batch.begin();

		for (int x = renderer.getCullStartX(2); x < renderer.getCullEndX(2); x++) {
			for (int y = renderer.getCullStartY(2); y < renderer.getCullEndY(2); y++) {
				
				if (!Settings.smoothLighting) {
					batch.setColor(setTempColor(x, y));
					main.fillRect(renderer.convertWorldX(x),
							renderer.convertWorldY(y, World.tilesizey), World.tilesizex,
							World.tilesizey);
					continue;
				}

				main.drawGradient(batch, renderer.convertWorldX(x),
						renderer.convertWorldY(y, World.tilesizey), World.tilesizex,
						World.tilesizey,
						tempColor3.set(setAveragedColors(x, y, x - 1, y + 1, x - 1, y, x, y + 1)),
						tempColor4.set(setAveragedColors(x, y, x + 1, y + 1, x + 1, y, x, y + 1)),
						tempColor5.set(setAveragedColors(x, y, x + 1, y - 1, x + 1, y, x, y - 1)),
						tempColor6.set(setAveragedColors(x, y, x - 1, y - 1, x - 1, y, x, y - 1)));
			}
		}

		batch.end();
		batch.setColor(1, 1, 1, 1);
	}

	public void updateLighting(int prex, int prey, int postx, int posty) {
		long nano = System.nanoTime();

		resetLighting(prex, prey, postx, posty);

		floodFillLighting(prex, prey, postx, posty);

		while (lightingUpdates.size > 0) {
			LightingUpdate l = lightingUpdates.pop();
			lightingUpdatePool.free(l);
		}

		lastUpdateLengthNano = (int) (System.nanoTime() - nano);
	}

	private Color setTempColor(int x, int y) {
		Color.rgb888ToColor(tempColor, getLightColor(x, y));

		tempColor.set(tempColor.lerp(shadowColor, calcAlpha(x, y)));

		return tempColor.set(tempColor.r, tempColor.g, tempColor.b, calcAlpha(x, y));
	}

	private Color setAveragedColors(int startx, int starty, int x1, int y1, int x2, int y2, int x3,
			int y3) {
		float r, g, b, a;
		float r1, g1, b1, a1;
		float r2, g2, b2, a2;
		float r3, g3, b3, a3;

		setTempColor(startx, starty);
		r = tempColor.r;
		g = tempColor.g;
		b = tempColor.b;
		a = tempColor.a;

		setTempColor(x1, y1);
		r1 = tempColor.r;
		g1 = tempColor.g;
		b1 = tempColor.b;
		a1 = tempColor.a;

		setTempColor(x2, y2);
		r2 = tempColor.r;
		g2 = tempColor.g;
		b2 = tempColor.b;
		a2 = tempColor.a;

		setTempColor(x3, y3);
		r3 = tempColor.r;
		g3 = tempColor.g;
		b3 = tempColor.b;
		a3 = tempColor.a;

		tempColor2
				.set((r + r1 + r2 + r3) / 4f, (g + g1 + g2 + g3) / 4f, (b + b1 + b2 + b3) / 4f, (a + a1 + a2 + a3) / 4f);

		return tempColor2;
	}

	private Color setToAmbient(int x, int y) {
		return tempColor.set(daylightColor.r, daylightColor.g, daylightColor.b, calcAlpha(x, y));
	}

	public float calcAlpha(int x, int y) {
		byte brightness = getBrightness(x, y);

		float alpha = (1 - ((brightness / 127f)));

		float threshold = 0.7f;
		if (alpha >= threshold) {
			float remainder = alpha - threshold;
			remainder = (remainder) * (1 / (1 - threshold));

			return remainder + (1f - threshold);
		} else {
			float remainder = alpha - (1 - threshold);
			remainder = (remainder) * (1 / (1 - remainder));

			return remainder * (1 - threshold);
		}
	}

	public void resetLighting(int originx, int originy, int width, int height) {
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);

		for (int x = originx; x < width; x++) {
			for (int y = originy; y < height; y++) {
				setBrightness((byte) 0, x, y);
				setLightColor(Color.rgb888(0, 0, 0), x, y);
				setIsPartOfSky(false, x, y);
			}

			int y = 0;
			boolean terminate = false;
			while (!terminate) {
				if (y >= sizey) {
					terminate = true;
					break;
				}
				if (((world.getBlock(x, y).isSolid(world, x, y) & BlockFaces.UP) == BlockFaces.UP)) {
					terminate = true;
					
					setToAmbientLight(x, y);
					break;
				}

				setBrightness((byte) (lastDayBrightness * 127f), x, y);
				setLightColor(Color.rgb888(daylightColor), x, y);
				setIsPartOfSky(true, x, y);
				y++;
			}

		}
	}
	
	private void setToAmbientLight(int x, int y){
		lightingUpdates.add(lightingUpdatePool.obtain().set(x, y, (byte) (lastDayBrightness * 127f),
				Color.rgb888(daylightColor)));
	}

	public void floodFillLighting(int originx, int originy, int width, int height) {
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);

		copyToTemp();

		lightingUpdateMethodCalls = 0;

		for (int i = lightingUpdates.size - 1; i >= 0; i--) {
			LightingUpdate l = lightingUpdates.get(i);
			setBrightness(l.brightness, l.x, l.y);
			mixColors(l.x, l.y, l.color);

			recursiveLight(l.x, l.y, l.brightness, l.color, true);
		}
	}

	private void copyToTemp() {
		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				tempBrightness[x][y] = brightness[x][y];
				tempLightColor[x][y] = lightColor[x][y];
			}
		}
	}

	private void recursiveLight(int x, int y, byte bright, int color, boolean source) {
		lightingUpdateMethodCalls++;
		
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
		Color.rgb888ToColor(tempColor, color);
		Color.rgb888ToColor(tempColor2, getLightColor(x, y));

		Color parameter = tempColor;
		Color atPosition = tempColor2;

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
		brightness[x][y] = l;
	}

	public void setLightColor(int color, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		lightColor[x][y] = color;
	}
	
	private void setIsPartOfSky(boolean b, int x, int y){
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		isPartOfSky[x][y] = b;
	}

	public byte getBrightness(int posx, int posy) {
		int x = posx;
		int y = posy;
		if (x < 0) x = 0;
		if (x >= sizex) x = sizex - 1;
		if(y < 0) y = 0;
		if(y >= sizey) y = sizey - 1;
		
		return brightness[x][y];
	}

	private byte getTempBrightness(int posx, int posy) {
		int x = posx;
		int y = posy;
		if(x < 0) x = 0;
		if(x >= sizex) x = sizex - 1;
		if(y < 0) y = 0;
		if(y >= sizey) y = sizey - 1;
		
		return tempBrightness[x][y];
	}

	public int getLightColor(int posx, int posy) {
		int x = posx;
		int y = posy;
		if(x < 0) x = 0;
		if(x >= sizex) x = sizex - 1;
		if(y < 0) y = 0;
		if(y >= sizey) y = sizey - 1;
		
		return lightColor[x][y];
	}

	private int getTempLightColor(int posx, int posy) {
		int x = posx;
		int y = posy;
		if(x < 0) x = 0;
		if(x >= sizex) x = sizex - 1;
		if(y < 0) y = 0;
		if(y >= sizey) y = sizey - 1;
		
		return tempLightColor[x][y];
	}
	
	private boolean getIsPartOfSky(int posx, int posy) {
		int x = posx;
		int y = posy;
		if(x < 0) x = 0;
		if(x >= sizex) x = sizex - 1;
		if(y < 0) y = 0;
		if(y >= sizey) y = sizey - 1;
		
		return isPartOfSky[x][y];
	}

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
