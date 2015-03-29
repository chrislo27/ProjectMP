package projectmp.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.world.World;


public class GameScreen extends Updateable{

	public GameScreen(Main m) {
		super(m);
		
		renderer = new WorldRenderer(main, world);
	}

	public World world;
	
	public WorldRenderer renderer;
	
	@Override
	public void render(float delta) {
		renderer.updateCameraAndSetMatrices();
		renderer.renderWorld();
		main.batch.setProjectionMatrix(main.camera.combined);
		renderer.renderHUD();
	}

	@Override
	public void renderUpdate() {
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			main.client.close();
			Main.logger.info("Connection closed");
			Main.ERRORMSG.setMessage("Disconnected from server: client closed connection");
			main.setScreen(Main.ERRORMSG);
		}
	}
	
	public void newWorld(World world){
		this.world = world;
		renderer.world = world;
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		
	}

}
