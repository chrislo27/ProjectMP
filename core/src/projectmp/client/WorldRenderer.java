package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.block.Block.BlockFaces;
import projectmp.common.entity.Entity;
import projectmp.common.util.AssetMap;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
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
	public SpriteBatch batch;
	public SmoothCamera camera;
	public World world;

	FrameBuffer worldBuffer;

	public WorldRenderer(Main m, World w) {
		main = m;
		batch = main.batch;
		world = w;

		camera = new SmoothCamera(world);

		worldBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, true);
	}

	public void renderWorld() {
		camera.update();

		// world to buffer
		worldBuffer.begin();
		batch.begin();

		batch.setColor(0.4f, 0.4f, 0.6f, 1);
		main.fillRect(0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(1, 1, 1, 1);

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
			if (e == Main.GAME.getPlayer()) {
				continue;
			}
			e.render(this);
		}
		if (Main.GAME.getPlayer() != null) Main.GAME.getPlayer().render(this);

		batch.setColor(1, 1, 1, 1);
		batch.end();

		worldBuffer.end();
		// end world to buffer

		// draw everything
		batch.begin();
		batch.draw(worldBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);
		batch.end();

		world.lightingEngine.render(this, batch);
	}

	public void renderHUD() {
		batch.begin();
		batch.end();
	}
	
	public void tickUpdate(){

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
	}

}
