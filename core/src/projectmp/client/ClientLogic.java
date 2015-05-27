package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.Inventory;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryonet.Client;

public class ClientLogic implements Disposable {

	public static final float TIME_BETWEEN_FORCE_UPDATE = 2.5f;

	public Main main;
	public Client client;

	public World world;
	public WorldRenderer renderer;

	private PacketPlayerPosUpdate playerUpdate = new PacketPlayerPosUpdate();

	private int playerIndex = -1;

	public Inventory playerInventory = null;

	public ClientLogic(Main main) {
		this.main = main;
		client = main.client;

		renderer = new WorldRenderer(main, world, this);
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

	public void newWorld(World world) {
		this.world = world;
		renderer.world = world;
		renderer.camera.setWorld(world);
		this.world.isServer = false;
	}

	public void tickUpdate() {
		renderer.tickUpdate();
		world.tickUpdate();

		if (getPlayer() != null) {
			if (main.client.isConnected()) {
				getPlayer().movementAndCollision();
				getPlayer().positionUpdate(getPlayer().x, getPlayer().y);

				// send a movement update if the player moved last tick OR if it's time to send a packet (every 2.5 sec)
				if (getPlayer().hasMovedLastTick()
						|| (world.time.totalTicks
								% ((int) (Main.TICKS * TIME_BETWEEN_FORCE_UPDATE)) == 0)) {
					prepareMovementUpdate();
					main.client.sendUDP(playerUpdate);
				}
			} else {
				main.client.close();
				Main.ERRORMSG.setMessage(Translator.instance().getMsg("menu.msg.disconnected")
						+ Translator.instance().getMsg("menu.msg.connectionlost"));
				main.setScreen(Main.ERRORMSG);
			}

		}
	}

	public void render() {
		centerCameraOnPlayer();

		renderer.renderWorld();
		main.batch.setProjectionMatrix(main.camera.combined);
		renderer.renderHUD();
	}

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

	public void renderDebug(int starting) {
		main.font.draw(main.batch, "latency: " + main.client.getReturnTripTime() + " ms", 5,
				Main.convertY(starting));
		if (world != null) {
			main.font.draw(main.batch, "entities: " + world.entities.size, 5,
					Main.convertY(starting + 15));
			main.font.draw(main.batch,
					"lightingTimeTaken: " + (world.lightingEngine.getLastUpdateLength() / 1000000f)
							+ " ms", 5, Main.convertY(starting + 30));
			main.font.draw(main.batch, "worldTime: " + world.time.currentDayTicks
					+ ", lastDayBri: " + world.lightingEngine.lastDayBrightness, 5,
					Main.convertY(starting + 45));
			main.font.draw(main.batch, "timeOfDay: " + world.time.getCurrentTimeOfDay(), 5,
					Main.convertY(starting + 60));
			main.font.draw(main.batch, "x: " + getPlayer().x, 5, Main.convertY(starting + 75));
			main.font.draw(main.batch, "y: " + getPlayer().y, 5, Main.convertY(starting + 90));
			main.font.draw(main.batch, "cursorx: "
					+ ((int) ((Main.getInputX() + renderer.camera.camerax) / World.tilesizex)), 5,
					Main.convertY(starting + 105));
			main.font.draw(main.batch, "cursory: "
					+ ((int) ((Main.getInputY() + renderer.camera.cameray) / World.tilesizey)), 5,
					Main.convertY(starting + 120));
			main.font
					.draw(main.batch,
							"lightlevel: "
									+ world.lightingEngine.getBrightness(
											((int) ((Main.getInputX() + renderer.camera.camerax) / World.tilesizex)),
											((int) ((Main.getInputY() + renderer.camera.cameray) / World.tilesizey))),
							5, Main.convertY(starting + 135));
			main.font.draw(main.batch, "weather: "
					+ (world.getWeather() == null ? null : world.getWeather().getClass()
							.getSimpleName()), 5, Main.convertY(starting + 150));
		}
	}

	private void prepareMovementUpdate() {
		playerUpdate.username = Main.username;
		playerUpdate.x = getPlayer().x;
		playerUpdate.y = getPlayer().y;
		playerUpdate.velox = getPlayer().velox;
		playerUpdate.veloy = getPlayer().veloy;
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
			getPlayer().jump();
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

			world.lightingEngine.floodFillLighting(prex, prey, postx, posty);
		}
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}

}
