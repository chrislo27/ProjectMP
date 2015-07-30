package projectmp.client;

import java.util.Map.Entry;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Particle;
import projectmp.common.util.Utils;
import projectmp.common.util.render.LiquidContainer;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
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
	private FrameBuffer bypassBuffer;
	private FrameBuffer healthBuffer;

	private float seconds = 0;

	private boolean isBypassing = false;

	private float lastPlayerHealth = -1;
	private LiquidContainer healthLiquid;
	private float vignetteBloodAlpha = 0;

	public WorldRenderer(Main m, World w, ClientLogic l) {
		main = m;
		batch = main.batch;
		world = w;
		logic = l;

		camera = new SmoothCamera(world);

		healthLiquid = new LiquidContainer(64, 64, 2, 8, new Color(1, 0, 0, 1));

		worldBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, false);
		lightingBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, false);
		bypassBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, false);
		healthBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, false);
	}

	public void renderWorld() {

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		renderToLightingBuffer();

		renderToWorldBuffer();

		/* -------------------------------------------- */

		// render background
		renderBackground();

		batch.begin();

		// draw the world buffer for full light
		batch.draw(worldBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);
		if (world.getWeather() != null) {
			world.getWeather().renderOnWorld(this);
		}

		batch.flush();

		// draw the lighting buffer, masked with the world buffer texture on top of world buffer
		batch.setShader(main.maskshader);
		Main.useMask(worldBuffer.getColorBufferTexture());
		batch.draw(lightingBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);
		batch.setShader(null);

		// draw bypass buffer
		batch.draw(bypassBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);

		batch.end();
	}

	private void renderBackground() {
		// render background
		batch.begin();
		batch.setColor(1, 1, 1, 1);

		world.background.render(this);

		batch.end();
	}

	private void renderToWorldBuffer() {
		// clear bypass buffer
		bypassBuffer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		bypassBuffer.end();

		// world to buffer
		worldBuffer.begin();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		// culling with 0 offsets
		int prex = getCullStartX(0);
		int prey = getCullStartY(0);
		int postx = getCullEndX(0);
		int posty = getCullEndY(0);

		boolean isSetForBreaking = false;

		int greatestRenderingLevel = 0;

		for (int layer = 0; layer <= greatestRenderingLevel; layer++) {
			for (int y = posty; y >= prey; y--) {
				for (int x = prex; x < postx; x++) {
					int blockLayer = world.getBlock(x, y).getRenderingLayer(world, x, y);

					// if the block's rendering layer is greater than the greatest so far, replace
					if (blockLayer > greatestRenderingLevel) {
						greatestRenderingLevel = blockLayer;
					}

					// render if only the rendering layer matches the current one
					if (world.getBlock(x, y).getRenderingLayer(world, x, y) == layer) {
						boolean isBeingBroken = world.getBreakingProgress(x, y) > 0;

						if (isBeingBroken) {
							if (!isSetForBreaking) {
								isSetForBreaking = true;
								batch.setShader(main.maskNoiseShader);
							}

							seconds += Gdx.graphics.getDeltaTime()
									* (0.75f + ((MathHelper.clampNumberFromTime(2f) * 2) * 0.25f));

							main.maskNoiseShader.setUniformf("time", seconds);
							main.maskNoiseShader.setUniformf("speed", 1f);
							if (x == logic.getCursorBlockX() && y == logic.getCursorBlockY()) {
								main.maskNoiseShader.setUniformf("outlinecolor", 0f, 1f, 1f, 1f);
							} else {
								main.maskNoiseShader.setUniformf("outlinecolor", 0f, 0f, 0f, 0f);
							}
							Utils.setupMaskingNoiseShader(main.maskNoiseShader,
									MathUtils.clamp(world.getBreakingProgress(x, y), 0f, 1f));
						} else {
							if (isSetForBreaking) {
								isSetForBreaking = false;
								batch.setShader(null);
							}
						}

						renderBlockInWorld(x, y);

						if (isSetForBreaking) {
							// better than re-setting the shader to default each time
							batch.flush();
						}
					}
				}
			}

			if (isSetForBreaking) {
				isSetForBreaking = false;
				batch.setShader(null);
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

				// render using item
				for (Entry<String, Integer> entry : logic.getOtherPlayersSelected().entrySet()) {
					EntityPlayer p = world.getPlayerByUsername(entry.getKey());

					if (p == null) continue;
					if (MathHelper.intersects(p.x, p.y, p.sizex, p.sizey, getCullStartX(8),
							getCullStartY(8), getCullEndX(8), getCullEndY(8)) == false) continue;

					ItemStack slotItem = p.getInventoryObject().getSlot(entry.getValue());
					long cursor = logic.getOtherPlayerCursor(entry.getKey());

					if (slotItem.isNothing()) continue;

					slotItem.getItem().onUsingRender(this, p, entry.getValue(),
							Utils.unpackLongUpper(cursor), Utils.unpackLongLower(cursor));
				}

				if (logic.isUsingItem() && !logic.getSelectedItem().isNothing()) {
					logic.getSelectedItem()
							.getItem()
							.onUsingRender(this, logic.getPlayer(), logic.selectedItem,
									logic.getCursorBlockX(), logic.getCursorBlockY());
				}
			}
		}

		for (int i = 0; i < world.particles.size; i++) {
			Particle p = world.particles.get(i);

			p.render(this, main);
		}

		batch.setColor(1, 1, 1, 1);
		batch.end();

		worldBuffer.end();
	}

	private void renderToLightingBuffer() {
		// lighting to buffer
		lightingBuffer.begin();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.lightingEngine.render(this, batch);

		lightingBuffer.end();
	}

	public void renderHUD() {
		if (vignetteBloodAlpha > 0) {
			vignetteBloodAlpha -= Gdx.graphics.getDeltaTime();
		}

		if (vignetteBloodAlpha <= 0) {
			vignetteBloodAlpha = 0;
		}

		batch.begin();

		// render weather
		if (world.getWeather() != null) {
			world.getWeather().renderHUD(this);
		}

		// render vignette
		batch.setColor(0, 0, 0, 0.1f);
		batch.draw(AssetRegistry.getTexture("vignette"), 0, 0, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT);
		batch.setColor(1, 0, 0, vignetteBloodAlpha);
		batch.draw(AssetRegistry.getTexture("vignette"), 0, 0, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT);
		batch.setColor(1, 1, 1, 1);
		
		batch.end();

		// health liquid

		healthBuffer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		healthLiquid.render(main, 64, 64);
		healthBuffer.end();

		batch.begin();

		Texture healthbg = AssetRegistry.getTexture("healthbg");
		batch.draw(healthbg, 0, Settings.DEFAULT_HEIGHT, AssetRegistry.getTexture("healthbg")
				.getWidth(), -AssetRegistry.getTexture("healthbg").getHeight());

		batch.flush();

		batch.setShader(main.maskshader);
		Main.useMask(AssetRegistry.getTexture("heartmask"));

		batch.draw(healthBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);

		batch.setShader(null);
		batch.flush();

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
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			logic.getPlayer().damage(5);
		}

		// took dmg
		if (lastPlayerHealth > (logic.getPlayer().health * 1f / logic.getPlayer().maxhealth)) {
			// vignette and liquid
			healthLiquid.height = 64 * (logic.getPlayer().health * 1f / logic.getPlayer().maxhealth);
			healthLiquid.perturbAll(5f);

			vignetteBloodAlpha = Math.abs(((lastPlayerHealth - logic.getPlayer().health * 1f
					/ logic.getPlayer().maxhealth)) * 2.5f);
		}

		// reset
		if (lastPlayerHealth != (logic.getPlayer().health * 1f / logic.getPlayer().maxhealth)) {
			lastPlayerHealth = (logic.getPlayer().health * 1f / logic.getPlayer().maxhealth);
		}
	}

	public boolean isBypassing() {
		return isBypassing;
	}

	public void startBypassing() {
		if (isBypassing) {
			throw new IllegalStateException("Cannot start bypassing buffer while already started!");
		}

		batch.flush();

		worldBuffer.end();
		bypassBuffer.begin();
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		isBypassing = true;
	}

	public void stopBypassing() {
		if (!isBypassing) {
			throw new IllegalStateException("Cannot stop bypassing when already stopped!");
		}

		batch.flush();

		bypassBuffer.end();
		worldBuffer.begin();
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		isBypassing = false;
	}

	public void renderBlockInWorld(int x, int y) {
		world.getBlock(x, y).renderInWorld(this, convertWorldX(x),
				convertWorldY(y, World.tilesizex), World.tilesizex, World.tilesizey, x, y);
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
		bypassBuffer.dispose();
		healthBuffer.dispose();
	}

}
