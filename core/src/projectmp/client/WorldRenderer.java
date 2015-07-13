package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Particle;
import projectmp.common.world.World;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.CrtMonitor;
import com.bitfire.postprocessing.filters.CrtScreen.RgbMode;
import com.bitfire.utils.ShaderLoader;

public class WorldRenderer implements Disposable {

	public Main main;
	public ClientLogic logic;
	public SpriteBatch batch;
	public SmoothCamera camera;
	public World world;

	private FrameBuffer worldBuffer;
	private FrameBuffer lightingBuffer;

	private PostProcessor postProcessor;

	public WorldRenderer(Main m, World w, ClientLogic l) {
		main = m;
		batch = main.batch;
		world = w;
		logic = l;

		camera = new SmoothCamera(world);

		worldBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, false);
		lightingBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, false);

		ShaderLoader.BasePath = "postprocessing/";

		postProcessor = new PostProcessor(false, false,
				Gdx.app.getType() == ApplicationType.Desktop);

		Bloom bloom = new Bloom((int) (Settings.DEFAULT_WIDTH * 0.25f),
				(int) (Settings.DEFAULT_HEIGHT * 0.25f));
		bloom.setThreshold(0.5f);
		//postProcessor.addEffect(bloom);

		CrtMonitor monitor = new CrtMonitor(Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT, true,
				true, RgbMode.ChromaticAberrations, 0b111111);
		//postProcessor.addEffect(monitor);
	}

	public void renderWorld() {

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/* --------------------------------------------------------------------- */

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

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

		int greatestRenderingLevel = 0;

		for (int layer = 0; layer <= greatestRenderingLevel; layer++) {
			for (int x = prex; x < postx; x++) {
				for (int y = posty; y >= prey; y--) {
					int blockLayer = world.getBlock(x, y).getRenderingLayer(world, x, y);

					// if the block's rendering layer is greater than the greatest so far, replace
					if (blockLayer > greatestRenderingLevel) {
						greatestRenderingLevel = blockLayer;
					}

					// render if only the rendering layer matches the current one
					if (world.getBlock(x, y).getRenderingLayer(world, x, y) == layer) {
						if (x == logic.getCursorBlockX() && y == logic.getCursorBlockY()
								&& !logic.getPlayerInventory().getSelectedItem().isNothing()) {
							logic.getPlayerInventory()
									.getSelectedItem()
									.getItem()
									.onRenderCursorBlock(this,
											logic.getPlayerInventory().getSelectedItem(), x, y);
						} else {
							renderBlockInWorld(x, y);
						}
					}
				}
			}

			// render entities right after layer 0
			if (layer == 0) {
				for (int i = 0; i < world.getNumberOfEntities(); i++) {
					Entity e = world.getEntityByIndex(i);

					// culling
					if (MathHelper.intersects(0, 0, Settings.DEFAULT_WIDTH,
							Settings.DEFAULT_HEIGHT, convertWorldX(e.visualX),
							convertWorldY(e.visualY, 0), e.sizex * World.tilesizex, e.sizey
									* World.tilesizey)) {
						e.render(this);
					}
				}
			}
		}

		for (int i = 0; i < world.particles.size; i++) {
			Particle p = world.particles.get(i);

			p.render(world, main);
		}

		batch.setColor(1, 1, 1, 1);
		batch.end();

		worldBuffer.end();

		/* --------------------------------------------------------------------- */

		// begin capture for post processing
		postProcessor.capture();

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

		// draw the lighting buffer, masked with the world buffer texture
		batch.setShader(main.maskshader);
		Main.useMask(worldBuffer.getColorBufferTexture());
		batch.draw(lightingBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);
		batch.setShader(null);

		batch.end();

		postProcessor.render();

	}

	public void renderHUD() {
		batch.begin();

		// render weather
		if (world.getWeather() != null) {
			world.getWeather().renderHUD(this);
		}

		// render vignette
		batch.setColor(0, 0, 0, 0.1f);
		batch.draw(AssetRegistry.getTexture("vignette"), 0, 0, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT);
		batch.setColor(1, 1, 1, 1);

		if (logic.getCurrentGui() != null) {
			logic.getCurrentGui().render(this, logic);
		} else {
			// TODO render hotbar
		}

		batch.end();
	}

	public void renderPlayerNames() {
		batch.begin();

		// render player names
		world.main.font.setColor(1, 1, 1, 1);
		for (int i = 0; i < world.getNumberOfEntities(); i++) {
			Entity e = world.getEntityByIndex(i);

			if (e instanceof EntityPlayer) {
				if (logic.getPlayer() != e) {
					EntityPlayer p = (EntityPlayer) e;

					// culling
					if (!MathHelper.intersects(0, 0, Settings.DEFAULT_WIDTH,
							Settings.DEFAULT_HEIGHT, convertWorldX(p.visualX),
							convertWorldY(p.visualY, e.sizey * World.tilesizey), p.sizex
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

	public void tickUpdate() {

	}
	
	public void renderBlockInWorld(int x, int y){
		world.getBlock(x, y).renderIndexAt(batch, main, world,
				convertWorldX(x), convertWorldY(y, World.tilesizex),
				World.tilesizex, World.tilesizey,
				world.getBlock(x, y).getCurrentRenderingIndex(world, x, y), x,
				y);
	}

	protected void changeWorld(World w) {
		world = w;
	}

	/**
	 * Converts world coordinates to screen coordinates accounting for camera position
	 * @param worldX
	 * @return
	 */
	public float convertWorldX(float worldX) {
		return worldX * World.tilesizex - camera.camerax;
	}

	/**
	 * Converts world coordinates to screen coordinates accounting for camera position
	 * @param worldY
	 * @return
	 */
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

		postProcessor.dispose();
	}

}
