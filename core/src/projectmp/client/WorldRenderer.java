package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class WorldRenderer {
	
	public Main main;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public World world;
	
	public WorldRenderer(Main m, World w){
		main = m;
		batch = main.batch;
		world = w;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
	}

	public void updateCameraAndSetMatrices() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}
	
	public void renderWorld(){
		batch.begin();
		for(int x = 0; x < world.sizex; x++){
			for(int y = 0; y < world.sizey; y++){
				world.getBlock(x, y).render(this, x, y);
			}
		}
		batch.end();
	}
	
	public void renderHUD(){
		batch.begin();
		batch.end();
	}
	
	protected void changeWorld(World w){
		world = w;
	}
	
}
