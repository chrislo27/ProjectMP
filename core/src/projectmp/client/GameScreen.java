package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class GameScreen extends Updateable {

	public GameScreen(Main m) {
		super(m);

		renderer = new WorldRenderer(main, world);
	}

	public World world;
	public WorldRenderer renderer;

	private PacketPlayerPosUpdate playerUpdate = new PacketPlayerPosUpdate();

	private int playerIndex = -1;

	@Override
	public void render(float delta) {
		centerCameraOnPlayer();

		renderer.renderWorld();
		main.batch.setProjectionMatrix(main.camera.combined);
		renderer.renderHUD();
	}

	public EntityPlayer getPlayer() {
		if (world == null) return null;
		if (world.entities.size == 0) return null;
		if (playerIndex >= world.entities.size || playerIndex == -1) updatePlayerIndex();

		return (EntityPlayer) world.entities.get(playerIndex);
	}

	private void updatePlayerIndex() {
		playerIndex = -1;
		for (int i = 0; i < world.entities.size; i++) {
			if (world.entities.get(i) instanceof EntityPlayer) {
				if (((EntityPlayer) world.entities.get(i)).username.equals(Main.username)) {
					playerIndex = i;
					return;
				}
			}
		}
	}

	@Override
	public void renderUpdate() {
		playerInput();

		for (Entity e : world.entities) {
			e.clientRenderUpdate();
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.client.close();
			Main.logger.info("Connection closed");
			Main.ERRORMSG.setMessage("Disconnected from server: client closed connection");
			main.setScreen(Main.ERRORMSG);
		}
	}

	public void newWorld(World world) {
		this.world = world;
		renderer.world = world;
		renderer.camera.setWorld(world);
		this.world.isServer = false;
	}

	@Override
	public void tickUpdate() {
		renderer.tickUpdate();

		if (getPlayer() != null) {
			if (main.client.isConnected()) {
				getPlayer().tickUpdate();

				if (getPlayer().hasMovedLastTick()) {
					playerUpdate.username = Main.username;
					playerUpdate.x = getPlayer().x;
					playerUpdate.y = getPlayer().y;

					main.client.sendUDP(playerUpdate);
				}
			}

		}
		world.tickUpdate();
	}

	public void centerCameraOnPlayer() {
		if (getPlayer() != null) {
			renderer.camera.centerOn((getPlayer().x + getPlayer().sizex / 2f) * World.tilesizex,
					(getPlayer().y + getPlayer().sizey / 2f) * World.tilesizey);

			renderer.camera.clamp();
			renderer.camera.update();
		}
	}

	private void playerInput() {
		if (getPlayer() == null || !main.client.isConnected()) return;

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			getPlayer().moveLeft();
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			getPlayer().moveRight();
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			getPlayer().moveUp();
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			getPlayer().moveDown();
		}

		if (Gdx.input.isKeyJustPressed(Keys.L)) {
			int prex = (int) MathUtils.clamp(((renderer.camera.camerax / World.tilesizex) - 10),
					0f, world.sizex);
			int prey = (int) MathUtils.clamp(((renderer.camera.cameray / World.tilesizey) - 10),
					0f, world.sizey);
			int postx = (int) MathUtils.clamp((renderer.camera.camerax / World.tilesizex) + 20
					+ (Settings.DEFAULT_WIDTH / World.tilesizex), 0f, world.sizex);
			int posty = (int) MathUtils.clamp((renderer.camera.cameray / World.tilesizey) + 20
					+ (Settings.DEFAULT_HEIGHT / World.tilesizex), 0f, world.sizey);

			world.lightingEngine.resetLighting(prex, prey, postx, posty);

			world.lightingEngine.setLightSource((byte) 127, Color.rgb888(1, 0, 0),
					(int) (getPlayer().x) - 2, (int) getPlayer().y - 2);
			world.lightingEngine.setLightSource((byte) 127, Color.rgb888(0, 1, 0),
					(int) (getPlayer().x), (int) getPlayer().y);
			world.lightingEngine.setLightSource((byte) 127, Color.rgb888(0, 0, 1),
					(int) (getPlayer().x) + 2, (int) getPlayer().y);
			// world.lightingEngine.setLightSource((byte) 127, Color.rgb888(1, 1, 1), (int) (getPlayer().x), (int) getPlayer().y);

			world.lightingEngine.floodFillLighting(prex, prey, postx, posty);
		}
	}

	@Override
	public void renderDebug(int starting) {
		main.font.draw(main.batch, "latency: " + main.client.getReturnTripTime() + " ms", 5,
				Main.convertY(starting));
		if (world != null) {
			main.font.draw(main.batch, "entities: " + world.entities.size, 5,
					Main.convertY(starting + 15));
			main.font.draw(main.batch,
					"lightingTimeTaken: " + (world.lightingEngine.getLastUpdateLength() / 1000000f) + " ms", 5,
					Main.convertY(starting + 30));
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		if(world != null){
			world.lightingEngine.scheduleLightingUpdate();
		}
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
		renderer.dispose();
	}

}
