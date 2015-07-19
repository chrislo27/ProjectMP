package projectmp.client.lighting;

import java.util.List;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.block.Block.BlockFaces;
import projectmp.common.entity.Entity;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Sizeable;
import projectmp.common.world.TimeOfDay;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
	private Color blockCheckingColor = new Color();

	// all the light data
	private byte[][] skyLighting;
	private LightTiles lightData;

	// the lighting renderer
	private LightingRenderer lightingRenderer;

	// everything related to lighting updates
	private int lastUpdateLengthNano = 0;
	/**
	 * 0 = no update
	 * <br>
	 * 1 = normal update
	 * <br>
	 * 2 = sky update + normal after
	 */
	private int isUpdateScheduled = 0;
	private float lastUpdateCamX = 0;
	private float lastUpdateCamY = 0;
	private final Pool<LightingUpdate> lightingUpdatePool = Pools.get(LightingUpdate.class, 256);
	private Array<LightingUpdate> lightingUpdates = new Array<LightingUpdate>(256);

	/**
	 * used for debug
	 */
	private int lightingUpdateMethodCalls = 0;

	private LightingBoundingBox boundingbox = new LightingBoundingBox();
	private Vector2 entityLightOffset = new Vector2();

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
			scheduleLightingUpdate(false);
		}
		if (Math.abs((renderer.camera.cameray + (Settings.DEFAULT_HEIGHT / 2f)) - lastUpdateCamY) > Settings.DEFAULT_HEIGHT
				- (World.tilesizey * 2)) {
			scheduleLightingUpdate(false);
		}

		if (isUpdateScheduled > 0) {
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

			updateLighting(prex, prey, postx, posty, isUpdateScheduled >= 2);
			isUpdateScheduled = 0;
		}

		lightingRenderer.render(renderer, batch);
	}

	public void updateLighting(int prex, int prey, int postx, int posty, boolean shouldUpdateSky) {
		long nano = System.nanoTime();

		// reset the lighting and calculate the sky lights
		resetLightingAndCalcSky(prex, prey, postx, posty, shouldUpdateSky);

		// do all the lighting updates
		doLightingUpdates(prex, prey, postx, posty);

		// free the lighting updates
		while (lightingUpdates.size > 0) {
			LightingUpdate l = lightingUpdates.pop();
			lightingUpdatePool.free(l);
		}

		lastUpdateLengthNano = (int) (System.nanoTime() - nano);
	}

	private void resetLightingAndCalcSky(int originx, int originy, int width, int height,
			boolean shouldUpdateSky) {
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);

		for (int x = originx; x < width; x++) {
			for (int y = originy; y < height; y++) {
				// reset all
				if (shouldUpdateSky) skyLighting[x][y] = 0;
				setBrightness((byte) 0, x, y);
				setLightColor(Color.rgb888(0, 0, 0), x, y);
			}

			int y = 0;
			boolean terminate = false;
			while (!terminate && shouldUpdateSky) {
				if (((world.getBlock(x, y).isSolid(world, x, y) & BlockFaces.UP) == BlockFaces.UP)
						|| y >= sizey) {
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

	private void doLightingUpdates(int originx, int originy, int width, int height) {
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);

		lightingUpdateMethodCalls = 0;
		boundingbox.set(originx, originy, width, height);

		for (int x = originx; x < width; x++) {
			for (int y = originy; y < height; y++) {
				blockCheckingColor.set(world.getBlock(x, y).getLightEmitted(world, x, y));

				if (blockCheckingColor.a > 0) {
					onSource(x, y, (byte) (blockCheckingColor.a * 127f),
							Color.rgb888(blockCheckingColor));
				}
			}
		}

		List<Entity> withinRange = world.getQuadArea(boundingbox);
		for (int i = 0; i < withinRange.size(); i++) {
			Entity e = withinRange.get(i);

			e.setLightColor(blockCheckingColor);
			e.setLightOffset(entityLightOffset);

			if (blockCheckingColor.a > 0) {
				onSource((int) ((e.x + e.sizex / 2f) + entityLightOffset.x),
						(int) ((e.y + e.sizey / 2f) + entityLightOffset.y),
						(byte) (blockCheckingColor.a * 127f), Color.rgb888(blockCheckingColor));
			}
		}

		for (int i = lightingUpdates.size - 1; i >= 0; i--) {
			LightingUpdate l = lightingUpdates.get(i);

			onSource(l.x, l.y, l.brightness, l.color);
		}
	}

	protected void onSource(int x, int y, byte bright, int color) {
		// set source's brightness at block and colour
		setBrightness(bright, x, y);
		setLightColor(color, x, y);

		if (Settings.raycastedLighting) {
			int numberOfRays = Math.min(Math.round((bright * 2f * MathUtils.PI) / 2.225f), 360);
			int anglePerRay = (360 / numberOfRays);

			if (numberOfRays >= 8) {
				// rays for 90 degree offset 45 deg artifacts
				rayOfLight(x, y, bright, color, 1, 1);
				rayOfLight(x, y, bright, color, -1, 1);
				rayOfLight(x, y, bright, color, 1, -1);
				rayOfLight(x, y, bright, color, -1, -1);
			}

			for (int i = 0; i < numberOfRays; i++) {
				int angle = anglePerRay * i;
				float rise = MathUtils.cosDeg(angle);
				float run = MathUtils.sinDeg(angle);
				int accuracy = 10000;

				// force accuracy for rise and run at 45 degree angles to prevent artifacts -- done before this
				if ((angle + 45) % 90 == 0) continue;

				rayOfLight(x, y, bright, color, (int) (rise * accuracy), (int) (run * accuracy));
			}
		} else {
			recursiveLight(x, y, bright, color, true);
		}
	}

	private void recursiveSky(int x, int y, byte bright, boolean source) {
		if (bright <= 0) {
			return;
		}
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) {
			return;
		}
		if (skyLighting[x][y] >= bright && !source) {
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

	private void rayOfLight(int x, int y, byte bright, int color, int rise, int run) {
		lightingUpdateMethodCalls++;

		float remainingBrightness = bright;
		int currentX = x;
		int currentY = y;
		int currentColor = color;
		boolean xIsLonger = true;
		// this is y is x is longer, x otherwise
		int numerator = 0;

		// calculate if Y is longer than X
		if (Math.abs(rise) > Math.abs(run)) xIsLonger = false;

		while (remainingBrightness > 0) {
			// set brightness if higher
			if (getBrightness(currentX, currentY) < remainingBrightness) setBrightness(
					(byte) remainingBrightness, currentX, currentY);

			// mix colours, sets the colours adjacent
			mixColors(currentX - 1, currentY, currentColor);
			mixColors(currentX + 1, currentY, currentColor);
			mixColors(currentX, currentY + 1, currentColor);
			mixColors(currentX, currentY - 1, currentColor);

			boolean movedDiagonally = false;

			// move current position
			if (xIsLonger) {
				currentX += (int) Math.signum(run);
				numerator += Math.abs(rise);

				if (Math.abs(numerator) >= Math.abs(run)) {
					numerator -= Math.abs(rise);
					currentY += (int) Math.signum(rise);
					movedDiagonally = true;
				}
			} else {
				currentY += (int) Math.signum(rise);
				numerator += Math.abs(run);

				if (Math.abs(numerator) >= Math.abs(rise)) {
					numerator -= Math.abs(run);
					currentX += (int) Math.signum(run);
					movedDiagonally = true;
				}
			}

			// decrease remaining brightness
			remainingBrightness -= (world.getBlock(currentX, currentY).lightSubtraction(world,
					currentX, currentY) * 127f)
					* (movedDiagonally ? MathHelper.rootTwo : 1);

			// check if current position is out of bounds, if so break
			if (currentX < 0 || currentY < 0 || currentX >= world.sizex || currentY >= world.sizey) {
				break;
			}
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
		scheduleLightingUpdate(false);
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

	public byte getSkyLight(int posx, int posy) {
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
	public float getSkyLightFromTOD(byte light) {
		return MathUtils.clamp(light * lastDayBrightness, 0f, 127f);
	}

	public float getActualLighting(int posx, int posy) {
		int x = posx;
		int y = posy;
		if (x < 0) x = 0;
		if (x >= sizex) x = sizex - 1;
		if (y < 0) y = 0;
		if (y >= sizey) y = sizey - 1;

		byte brightness = getBrightness(x, y);
		float sky = getSkyLightFromTOD(getSkyLight(x, y));

		if (sky > brightness) {
			return MathUtils.clamp(sky, 0f, 127f);
		} else {
			return brightness;
		}
	}

	public LightingRenderer getRenderer() {
		return lightingRenderer;
	}

	// update related things below

	public int getLastUpdateLength() {
		return lastUpdateLengthNano;
	}

	public void scheduleLightingUpdate(boolean shouldUpdateSky) {
		if (world.isServer) return;
		isUpdateScheduled = 1 + (shouldUpdateSky ? 1 : 0);
	}

	public boolean isLightingUpdateScheduled() {
		return isUpdateScheduled > 0;
	}

	public int getLightingUpdateType() {
		return isUpdateScheduled;
	}

	private static class LightingBoundingBox implements Sizeable {

		public float x, y, width, height;

		@Override
		public float getX() {
			return x;
		}

		@Override
		public float getY() {
			return y;
		}

		@Override
		public float getWidth() {
			return width;
		}

		@Override
		public float getHeight() {
			return height;
		}

		public void set(float x, float y, float w, float h) {
			this.x = x;
			this.y = y;
			width = w;
			height = h;
		}

	}

}
