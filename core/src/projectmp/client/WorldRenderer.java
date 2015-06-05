package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.util.AssetMap;
import projectmp.common.util.MathHelper;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {

	public Main main;
	public ClientLogic logic;
	public SpriteBatch batch;
	public SmoothCamera camera;
	public World world;

	private FrameBuffer worldBuffer;
	private FrameBuffer lightingBuffer;

	public WorldRenderer(Main m, World w, ClientLogic l) {
		main = m;
		batch = main.batch;
		world = w;
		logic = l;

		camera = new SmoothCamera(world);

		worldBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, true);
		lightingBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, true);
	}

	public void renderWorld() {
		camera.update();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/* --------------------------------------------------------------------- */

		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);

		// lighting to buffer
		lightingBuffer.begin();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.lightingEngine.render(this, batch);

		lightingBuffer.end();

		// world to buffer
		worldBuffer.begin();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		int prex = getCullStartX(0);
		int prey = getCullStartY(0);
		int postx = getCullEndX(0);
		int posty = getCullEndY(0);
		for (int x = prex; x < postx; x++) {
			for (int y = posty; y >= prey; y--) {
				world.getBlock(x, y).render(this, x, y);
			}
		}

		for (int i = 0; i < world.entities.size; i++) {
			Entity e = world.entities.get(i);

			// culling
			if (MathHelper.intersects(0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT,
					convertWorldX(e.visualX), convertWorldY(e.visualY, 0), e.sizex
							* World.tilesizex, e.sizey * World.tilesizey)) {

				e.render(this);
			}
		}

		batch.setColor(1, 1, 1, 1);
		batch.end();

		worldBuffer.end();

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		/* --------------------------------------------------------------------- */

		// render background
		batch.begin();
		batch.setColor(1, 1, 1, 1);

		world.background.render(this);

		batch.end();

		// mask lighting buffer onto world buffer and render

		batch.begin();

		// draw the world buffer as-is
		batch.draw(worldBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);
		if (world.getWeather() != null) {
			world.getWeather().renderOnWorld(this);
		}

		// draw the lighting buffer, masked
		//batch.setShader(main.maskshader);
		//Main.useMask(worldBuffer.getColorBufferTexture());
		batch.draw(lightingBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);
		//batch.setShader(null);
		batch.flush();

		// render player names
		world.main.font.setColor(1, 1, 1, 1);
		for (int i = 0; i < world.entities.size; i++) {
			if (world.entities.get(i) instanceof EntityPlayer) {
				if (logic.getPlayer() != world.entities.get(i)) {
					EntityPlayer p = (EntityPlayer) world.entities.get(i);

					// culling
					if(!MathHelper.intersects(0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT,
					convertWorldX(p.visualX), convertWorldY(p.visualY, 0), p.sizex
							* World.tilesizex, p.sizey * World.tilesizey)) continue;
					
					batch.setColor(1, 1, 1, 0.25f);
					world.main.drawTextBg(world.main.font, p.username, convertWorldX(p.visualX
							+ (p.sizex / 2))
							- (world.main.font.getBounds(p.username).width / 2),
							convertWorldY(p.visualY - p.sizey, World.tilesizey * p.sizey) + 20);
				}
			}
		}

		batch.end();

	}

	public void renderHUD() {
		batch.begin();
		// render weather
		if (world.getWeather() != null) {
			world.getWeather().renderHUD(this);
		}

		// render vignette
		batch.setColor(0, 0, 0, 0.1f);
		batch.draw(main.manager.get(AssetMap.get("vignette"), Texture.class), 0, 0,
				Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(1, 1, 1, 1);

		// TODO render hotbar

		batch.end();
	}

	public void tickUpdate() {

	}

	protected void changeWorld(World w) {
		world = w;
	}

	public float convertWorldX(float worldX) {
		return worldX * World.tilesizex - camera.camerax;
	}

	public float convertWorldY(float worldY, float offset) {
		return Main.convertY(worldY * World.tilesizey - camera.cameray + offset);
	}

	public int getCullStartX(int extra) {
		return (int) MathUtils.clamp(((camera.camerax / World.tilesizex) - 1 - extra), 0f,
				world.sizex);
	}

	public int getCullStartY(int extra) {
		return (int) MathUtils.clamp(((camera.cameray / World.tilesizey) - 1 - extra), 0f,
				world.sizey);
	}

	public int getCullEndX(int extra) {
		return (int) MathUtils.clamp((camera.camerax / World.tilesizex) + 2 + extra
				+ (Settings.DEFAULT_WIDTH / World.tilesizex), 0f, world.sizex);
	}

	public int getCullEndY(int extra) {
		return (int) MathUtils.clamp((camera.cameray / World.tilesizey) + 2 + extra
				+ (Settings.DEFAULT_HEIGHT / World.tilesizex), 0f, world.sizey);
	}

	@Override
	public void dispose() {
		worldBuffer.dispose();
		lightingBuffer.dispose();
	}

}
