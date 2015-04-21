package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
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
				getPlayer().movementAndCollision();
				
				if (getPlayer().hasMovedLastTick()) {
					playerUpdate.username = Main.username;
					playerUpdate.x = getPlayer().x;
					playerUpdate.y = getPlayer().y;

					main.client.sendUDP(playerUpdate);
				}
			} else {
				main.client.close();
				Main.ERRORMSG.setMessage(Translator.instance().getMsg("menu.msg.disconnected")
						+ Translator.instance().getMsg("menu.msg.connectionlost"));
				main.setScreen(Main.ERRORMSG);
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

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			greenx = ((int) ((Main.getInputX() + renderer.camera.camerax) / World.tilesizex));
			greeny = ((int) ((Main.getInputY() + renderer.camera.cameray) / World.tilesizey));
			
			world.lightingEngine.setLightSource((byte) 127, Color.rgb888(1, 0, 0), redx, redy);
			world.lightingEngine.setLightSource((byte) 127, Color.rgb888(0, 1, 0), greenx, greeny);
		}
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			redx = ((int) ((Main.getInputX() + renderer.camera.camerax) / World.tilesizex));
			redy = ((int) ((Main.getInputY() + renderer.camera.cameray) / World.tilesizey));

			world.lightingEngine.setLightSource((byte) 127, Color.rgb888(1, 0, 0), redx, redy);
			world.lightingEngine.setLightSource((byte) 127, Color.rgb888(0, 1, 0), greenx, greeny);
		}
	}
	
	private int greenx = 0;
	private int greeny = 0;
	private int redx = 0;
	private int redy = 0;

	@Override
	public void renderDebug(int starting) {
		main.font.draw(main.batch, "latency: " + main.client.getReturnTripTime() + " ms", 5,
				Main.convertY(starting));
		if (world != null) {
			main.font.draw(main.batch, "entities: " + world.entities.size, 5,
					Main.convertY(starting + 15));
			main.font.draw(main.batch,
					"lightingTimeTaken: " + (world.lightingEngine.getLastUpdateLength() / 1000000f)
							+ " ms", 5, Main.convertY(starting + 30));
			main.font.draw(main.batch,
					"worldTime: " + world.worldTime.currentDayTicks + ", lastDayBri: " + world.lightingEngine.lastDayBrightness, 5, Main.convertY(starting + 45));
			main.font.draw(main.batch,
					"timeOfDay: " + world.worldTime.getCurrentTimeOfDay(), 5, Main.convertY(starting + 60));
			main.font.draw(main.batch,
					"x: " + getPlayer().x, 5, Main.convertY(starting + 75));
			main.font.draw(main.batch,
					"y: " + getPlayer().y, 5, Main.convertY(starting + 90));
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		if (world != null) {
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
