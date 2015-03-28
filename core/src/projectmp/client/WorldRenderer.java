package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class WorldRenderer {
	
	Main main;
	SpriteBatch batch;
	OrthographicCamera camera;
	World world;
	
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
		
	}
	
	public void renderHUD(){
		
	}
	
	protected void changeWorld(World w){
		world = w;
	}
	
}
