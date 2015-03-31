package projectmp.client;

import projectmp.common.Main;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class GameScreen extends Updateable {

	public GameScreen(Main m) {
		super(m);

		renderer = new WorldRenderer(main, world);
	}

	public World world;
	public WorldRenderer renderer;
	
	public EntityPlayer player;

	@Override
	public void render(float delta) {
		renderer.renderWorld();
		main.batch.setProjectionMatrix(main.camera.combined);
		renderer.renderHUD();

		main.batch.begin();
		main.font.draw(main.batch, "x, y: " + renderer.camera.camerax + ", "
				+ renderer.camera.cameray, 5, Main.convertY(100));
		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.client.close();
			player = null;
			Main.logger.info("Connection closed");
			Main.ERRORMSG.setMessage("Disconnected from server: client closed connection");
			main.setScreen(Main.ERRORMSG);
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			renderer.camera.translate(-8, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			renderer.camera.translate(8, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			renderer.camera.translate(0, -8);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			renderer.camera.translate(0, 8);
		}

		renderer.camera.clamp();
		
		for(Entity e : world.entities){
			e.clientRenderUpdate();
		}
	}

	public void newWorld(World world) {
		this.world = world;
		renderer.world = world;
		renderer.camera.setWorld(world);
		
	}

	@Override
	public void tickUpdate() {
		if(player != null){
			if(main.client.isConnected()){
				player.tickUpdate();
			}
		}
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
