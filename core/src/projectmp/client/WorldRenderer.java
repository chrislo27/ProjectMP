package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class WorldRenderer {

	public Main main;
	public SpriteBatch batch;
	public SmoothCamera camera;
	public World world;

	public WorldRenderer(Main m, World w) {
		main = m;
		batch = main.batch;
		world = w;

		camera = new SmoothCamera(world);
	}

	public void renderWorld() {
		camera.update();
		batch.begin();
		
		int prex = (int) MathUtils.clamp(((camera.camerax / World.tilesizex) - 1), 0f, world.sizex);
		int prey = (int) MathUtils.clamp(((camera.cameray / World.tilesizey) - 1), 0f, world.sizey);
		int postx = (int) MathUtils.clamp((camera.camerax / World.tilesizex) + 2
				+ (Settings.DEFAULT_WIDTH / World.tilesizex), 0f, world.sizex);
		int posty = (int) MathUtils.clamp((camera.cameray / World.tilesizey) + 2
				+ (Settings.DEFAULT_HEIGHT / World.tilesizex), 0f, world.sizey);
		for(int x = prex; x < postx; x++){
			for(int y = posty; y >= prey; y--){
				world.getBlock(x, y).render(this, x, y);
			}
		}
		
		for(Entity e : world.entities){
			e.render(this);
		}
		batch.end();
	}

	public void renderHUD() {
		batch.begin();
		batch.end();
	}

	protected void changeWorld(World w) {
		world = w;
	}

}
