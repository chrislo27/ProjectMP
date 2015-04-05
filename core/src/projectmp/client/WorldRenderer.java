package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.entity.Entity;
import projectmp.common.util.AssetMap;
import projectmp.common.world.World;

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
	FrameBuffer lightBuffer;

	public WorldRenderer(Main m, World w) {
		main = m;
		batch = main.batch;
		world = w;

		camera = new SmoothCamera(world);

		worldBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, true);
		lightBuffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH,
				Settings.DEFAULT_HEIGHT, true);
	}

	public void renderWorld() {
		camera.update();

		// world to buffer
		worldBuffer.begin();
		batch.begin();

		batch.setColor(0, 0, 0, 1);
		main.fillRect(0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(1, 1, 1, 1);

		int prex = (int) MathUtils.clamp(((camera.camerax / World.tilesizex) - 1), 0f, world.sizex);
		int prey = (int) MathUtils.clamp(((camera.cameray / World.tilesizey) - 1), 0f, world.sizey);
		int postx = (int) MathUtils.clamp((camera.camerax / World.tilesizex) + 2
				+ (Settings.DEFAULT_WIDTH / World.tilesizex), 0f, world.sizex);
		int posty = (int) MathUtils.clamp((camera.cameray / World.tilesizey) + 2
				+ (Settings.DEFAULT_HEIGHT / World.tilesizex), 0f, world.sizey);
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
		batch.end();
		worldBuffer.end();
		// end world to buffer

		// light to buffer
		lightBuffer.begin();
		batch.begin();

		batch.setColor(0, 0, 0, 1);
		main.fillRect(0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(1, 1, 1, 1);

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		{
			batch.setColor(0, 0, 1, 1f);
			batch.draw(main.manager.get(AssetMap.get("featheredcircle"), Texture.class),
					1080 / 2 - 128, 720 / 2 - 128, 256, 256);
			batch.setColor(1, 0, 0, 1f);
			batch.draw(main.manager.get(AssetMap.get("featheredcircle"), Texture.class), 1080 / 2,
					720 / 2 - 128, 256, 256);
			batch.setColor(0, 1, 0, 1f);
			batch.draw(main.manager.get(AssetMap.get("featheredcircle"), Texture.class), 1080 / 2,
					720 / 2 - 300, 256, 256);
			batch.setColor(1, 1, 1, 1);
		}
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		batch.end();
		lightBuffer.end();
		// end light to buffer

		// draw everything
		batch.begin();
		batch.draw(worldBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);

		batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.draw(lightBuffer.getColorBufferTexture(), 0, Settings.DEFAULT_HEIGHT,
				Settings.DEFAULT_WIDTH, -Settings.DEFAULT_HEIGHT);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		batch.end();
	}

	public void renderHUD() {
		batch.begin();
		batch.end();
	}

	protected void changeWorld(World w) {
		world = w;
	}

	public float convertWorldX(float worldX) {
		return worldX * World.tilesizex - camera.camerax;
	}

	public float convertWorldY(float worldY) {
		return Main.convertY(worldY * World.tilesizey - camera.cameray);
	}

	@Override
	public void dispose() {
		worldBuffer.dispose();
		lightBuffer.dispose();
	}

}
