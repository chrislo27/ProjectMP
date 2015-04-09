package projectmp.client.lighting;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.block.Block.BlockFaces;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	
	private boolean[][] canSeeSky;

	private Color ambient = new Color(0, 0, 0, 1);
	private Color tempColor = new Color();
	private Color tempColor2 = new Color();

	private int lastUpdateLengthNano = 0;

	private boolean isUpdateScheduled = false;
	private float lastUpdateCamX = 0;
	private float lastUpdateCamY = 0;

	private final Pool<LightingUpdate> lightingUpdatePool = Pools.get(LightingUpdate.class, 256);
	private Array<LightingUpdate> lightingUpdates = new Array<LightingUpdate>(256);

	public LightingEngine(World world) {
		this.world = world;
		main = this.world.main;
		sizex = world.sizex;
		sizey = world.sizey;

		brightness = new byte[sizex][sizey];
		tempBrightness = new byte[sizex][sizey];
		lightColor = new int[sizex][sizey];
		tempLightColor = new int[sizex][sizey];
		canSeeSky = new boolean[sizex][sizey];

		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				lightColor[x][y] = Color.rgb888(0, 0, 0);
				canSeeSky[x][y] = false;
			}
		}

		copyToTemp();
	}

	/**
	 * call NOT between batch begin/end
	 */
	public void render(WorldRenderer renderer, SpriteBatch batch) {
		if (Math.abs((renderer.camera.camerax + (Settings.DEFAULT_WIDTH / 2f)) - lastUpdateCamX) > Settings.DEFAULT_WIDTH / 2f) {
			scheduleLightingUpdate();
		}
		if (Math.abs((renderer.camera.cameray + (Settings.DEFAULT_HEIGHT / 2f)) - lastUpdateCamY) > Settings.DEFAULT_HEIGHT / 2f) {
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
					+ (Settings.DEFAULT_WIDTH / World.tilesizex)
					+ (Settings.DEFAULT_WIDTH / World.tilesizex), 0f, world.sizex);
			int posty = (int) MathUtils.clamp((renderer.camera.cameray / World.tilesizey)
					+ (Settings.DEFAULT_HEIGHT / World.tilesizey)
					+ (Settings.DEFAULT_HEIGHT / World.tilesizex), 0f, world.sizey);

			lastUpdateCamX = renderer.camera.camerax + (Settings.DEFAULT_WIDTH / 2f);
			lastUpdateCamY = renderer.camera.cameray + (Settings.DEFAULT_HEIGHT / 2f);

			updateLighting(prex, prey, postx, posty);
			isUpdateScheduled = false;
		}

		ShapeRenderer shapes = main.shapes;

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapes.begin(ShapeType.Filled);

		for (int x = renderer.getCullStartX(2); x < renderer.getCullEndX(2); x++) {
			for (int y = renderer.getCullStartY(2); y < renderer.getCullEndY(2); y++) {
				shapes.setColor(setTempColor(x, y));
				shapes.rect(renderer.convertWorldX(x), renderer.convertWorldY(y), World.tilesizex,
						World.tilesizey);
			}
		}

		shapes.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

	}

	public void updateLighting(int prex, int prey, int postx, int posty) {
		resetLighting(prex, prey, postx, posty);

		for (int i = lightingUpdates.size - 1; i >= 0; i--) {
			LightingUpdate l = lightingUpdates.get(i);
			setBrightness(l.brightness, l.x, l.y);
			setLightColor(l.color, l.x, l.y);
		}

		floodFillLighting(prex, prey, postx, posty);

		while (lightingUpdates.size > 0) {
			LightingUpdate l = lightingUpdates.pop();
			lightingUpdatePool.free(l);
		}
	}

	private Color setTempColor(int x, int y) {
		Color.rgb888ToColor(tempColor, getLightColor(x, y));

		tempColor.set(tempColor.lerp(ambient, calcAlpha(x, y)));
		
		if(canSeeSky[x][y]){
			
		}

		return tempColor.set(tempColor.r, tempColor.g, tempColor.b, calcAlpha(x, y));
	}

	private Color setToAmbient(int x, int y) {
		return tempColor.set(ambient.r, ambient.g, ambient.b, calcAlpha(x, y));
	}

	public float calcAlpha(int x, int y) {
		byte brightness = getBrightness(x, y);
		if(canSeeSky[x][y]) brightness = 127;
		
		float alpha = (1 - ((brightness / 127f)));

		float threshold = 0.7f;
		if (alpha >= threshold) {
			float remainder = alpha - threshold;
			remainder = (remainder) * (1 / (1 - threshold));

			return remainder;
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
				canSeeSky[x][y] = false;
			}

			int y = 0;
			boolean terminate = false;
			while (!terminate) {
				if(((world.getBlock(x, y).isSolid(world, x, y) & BlockFaces.UP) == BlockFaces.UP)){
					terminate = true;
					// TODO set brightness and colour based on time of day
					setLightSource((byte) 127, Color.rgb888(0, 0, 0), x, y);
					break;
				}
				
				canSeeSky[x][y] = true;
				y++;
			}
			
		}
	}

	public void floodFillLighting(int originx, int originy, int width, int height) {
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);

		long nano = System.nanoTime();

		copyToTemp();

		for (int x = originx; x < width; x++) {
			for (int y = originy; y < height; y++) {
				if (getTempBrightness(x, y) > 0) {
					recursiveLight(x, y, (byte) (getTempBrightness(x, y)), getTempLightColor(x, y));
				}
			}
		}

		lastUpdateLengthNano = (int) (System.nanoTime() - nano);
	}

	private void copyToTemp() {
		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				tempBrightness[x][y] = brightness[x][y];
				tempLightColor[x][y] = lightColor[x][y];
			}
		}
	}

	private void recursiveLight(int x, int y, byte bright, int color) {
		mixColors(x, y, color);

		if (bright <= 0) {
			return;
		}
		if (getBrightness(x, y) > bright && bright > 0) {
			return;
		}
		if (x < 0 || y < 0 || x + 1 >= sizex || y + 1 >= sizey) {
			return;
		}
		if(canSeeSky[x][y]) return;

		setBrightness(bright, x, y);

		bright = (byte) MathUtils.clamp(bright - (world.getBlock(x, y).lightSubtraction(world, x, y) * 127), 0, 127);

		recursiveLight(x - 1, y, bright, color);
		recursiveLight(x, y - 1, bright, color);
		recursiveLight(x + 1, y, bright, color);
		recursiveLight(x, y + 1, bright, color);
	}

	private void mixColors(int x, int y, int color) {
		Color.rgb888ToColor(tempColor, color);
		Color.rgb888ToColor(tempColor2, getLightColor(x, y));

		Color parameter = tempColor;
		Color atPosition = tempColor2;

		parameter.lerp(atPosition, 0.985f);
		setLightColor(Color.rgb888(parameter), x, y);
		color = Color.rgb888(parameter);
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

	public byte getBrightness(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return 0;
		return brightness[x][y];
	}

	private byte getTempBrightness(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return 0;
		return tempBrightness[x][y];
	}

	public int getLightColor(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return Color.rgb888(1, 1, 1);
		return lightColor[x][y];
	}

	private int getTempLightColor(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return Color.rgb888(1, 1, 1);
		return tempLightColor[x][y];
	}

	public int getLastUpdateLength() {
		return lastUpdateLengthNano;
	}

	public void scheduleLightingUpdate() {
		if(world.isServer) return;
		isUpdateScheduled = true;
	}

	public boolean isLightingUpdateScheduled() {
		return isUpdateScheduled;
	}

}
