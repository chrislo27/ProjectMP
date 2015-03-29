package projectmp.client;

import projectmp.common.Main;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;

public class GameScreen extends Updateable {

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
		
		main.batch.begin();
		main.font.draw(main.batch, "x, y: " + renderer.camera.position.x + ", " + renderer.camera.position.y, 5, Main.convertY(100));
		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.client.close();
			Main.logger.info("Connection closed");
			Main.ERRORMSG.setMessage("Disconnected from server: client closed connection");
			main.setScreen(Main.ERRORMSG);
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			renderer.camera.translate(-8, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			renderer.camera.translate(8, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			renderer.camera.translate(0, 8, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			renderer.camera.translate(0, -8, 0);
		}

		renderer.camera.position.x = MathUtils.clamp(renderer.camera.position.x,
				renderer.camera.viewportWidth / 2, world.sizex * World.tilesizex
						- (renderer.camera.viewportWidth / 2));
		renderer.camera.position.y = MathUtils.clamp(renderer.camera.position.y,
				renderer.camera.viewportHeight / 2, world.sizey * World.tilesizey
						- (renderer.camera.viewportHeight / 2) + World.tilesizey);
	}

	public void newWorld(World world) {
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
