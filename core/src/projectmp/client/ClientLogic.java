package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Translator;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Gui;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.packet.PacketSwapSlot;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.util.Utils;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryonet.Client;

public class ClientLogic implements Disposable {

	public static final float TIME_BETWEEN_FORCE_UPDATE = 2.5f;

	public Main main;
	public Client client;

	public World world;
	public WorldRenderer renderer;

	private PacketPlayerPosUpdate playerUpdate = new PacketPlayerPosUpdate();
	private PacketSwapSlot swapSlot = new PacketSwapSlot();

	private int playerIndex = -1;

	private Gui currentGui = null;
	public ItemStack mouseStack = new ItemStack(null, 0);

	public ClientLogic(Main main) {
		this.main = main;
		client = main.client;

		renderer = new WorldRenderer(main, world, this);
	}

	public EntityPlayer getPlayer() {
		if (world == null) return null;
		if (world.getNumberOfEntities() <= 0) return null;
		if (playerIndex >= world.getNumberOfEntities() || playerIndex == -1) updatePlayerIndex();

		return (EntityPlayer) world.getEntityByIndex(playerIndex);
	}

	private void updatePlayerIndex() {
		playerIndex = -1;
		for (int i = 0; i < world.getNumberOfEntities(); i++) {
			Entity e = world.getEntityByIndex(i);

			if (e instanceof EntityPlayer) {
				if (((EntityPlayer) e).username.equals(Main.username)) {
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

				// send a movement update if the player moved last tick OR if it's time to send a packet
				if (getPlayer().hasMovedLastTick()
						|| (world.time.totalTicks
								% ((int) (Main.TICKS * TIME_BETWEEN_FORCE_UPDATE)) == 0)) {
					prepareMovementUpdate();
					main.client.sendUDP(playerUpdate);
				}
			} else {
				main.client.close();

				if (main.serverLogic.isSingleplayer) {
					main.serverLogic.isSingleplayer = false;
					main.server.close();

					main.setScreen(Main.MAINMENU);
				} else {
					Main.ERRORMSG.setMessage(Translator.instance().getMsg("menu.msg.disconnected")
							+ Translator.instance().getMsg("menu.msg.connectionlost"));
					main.setScreen(Main.ERRORMSG);
				}
			}

		}
	}

	public void render() {
		centerCameraOnPlayerAndUpdate();

		renderer.renderWorld();

		renderer.renderPlayerNames();

		main.batch.setProjectionMatrix(main.camera.combined);
		renderer.renderHUD();
	}

	public void renderUpdate() {
		playerInput();

		for (int i = 0; i < world.getNumberOfEntities(); i++) {
			Entity e = world.getEntityByIndex(i);

			e.clientRenderUpdate();
		}
	}

	public void renderDebug(int starting) {
		main.font.draw(main.batch, "latency: " + main.client.getReturnTripTime() + " ms", 5,
				Main.convertY(starting));
		if (world != null) {
			main.font.draw(main.batch, "entities: " + world.getNumberOfEntities(), 5,
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
			main.font.draw(main.batch, "cursorx: " + getCursorBlockX(), 5,
					Main.convertY(starting + 105));
			main.font.draw(main.batch, "cursory: " + getCursorBlockY(), 5,
					Main.convertY(starting + 120));
			main.font
					.draw(main.batch,
							"lightlevel: "
									+ world.lightingEngine.getActualLighting(
											getCursorBlockX(), getCursorBlockY()),
							5, Main.convertY(starting + 135));
			main.font.draw(main.batch, "weather: "
					+ (world.getWeather() == null ? null : world.getWeather().getClass()
							.getSimpleName()
							+ ", " + world.getWeather().getTimeRemaining() + " ticks left"), 5,
					Main.convertY(starting + 150));
			main.font.draw(main.batch, "cam x: " + renderer.camera.camerax, 5,
					Main.convertY(starting + 165));
			main.font.draw(main.batch, "cam y: " + renderer.camera.camerax, 5,
					Main.convertY(starting + 180));
			main.font.draw(main.batch, "cam wantedx: " + renderer.camera.wantedx, 5,
					Main.convertY(starting + 195));
			main.font.draw(main.batch, "cam wantedy: " + renderer.camera.wantedx, 5,
					Main.convertY(starting + 210));
			main.font.draw(main.batch, "visualX: " + getPlayer().visualX, 5,
					Main.convertY(starting + 225));
			main.font.draw(main.batch, "visualY: " + getPlayer().visualY, 5,
					Main.convertY(starting + 240));
			main.font.draw(main.batch, "lerpVeloX: " + getPlayer().lerpVeloX, 5,
					Main.convertY(starting + 255));
			main.font.draw(main.batch, "lerpVeloY: " + getPlayer().lerpVeloY, 5,
					Main.convertY(starting + 270));
		}
	}

	private void prepareMovementUpdate() {
		playerUpdate.username = Main.username;
		playerUpdate.x = getPlayer().x;
		playerUpdate.y = getPlayer().y;
		playerUpdate.velox = getPlayer().velox;
		playerUpdate.veloy = getPlayer().veloy;
	}

	public void centerCameraOnPlayerAndUpdate() {
		if (getPlayer() != null) {
			renderer.camera.centerOn((getPlayer().visualX + getPlayer().sizex / 2f)
					* World.tilesizex, (getPlayer().visualY + getPlayer().sizey / 2f)
					* World.tilesizey);

			renderer.camera.update();
		}
	}

	private void playerInput() {
		if (getPlayer() == null || !main.client.isConnected()) return;

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			getPlayer().moveLeft(false);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			getPlayer().moveRight(false);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			getPlayer().jump();
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {

		}

		if (getCurrentGui() == null) {
			int x = getBlockXCursor();
			int y = getBlockYCursor();

			if (Utils.isButtonJustPressed(Buttons.LEFT)) {

			} else if (Utils.isButtonJustPressed(Buttons.RIGHT)) {
				world.getBlock(x, y).onActivate(world, x, y, getPlayer());
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			if (getCurrentGui() == null) {
				openGui("playerInv", Utils.unpackLongUpper(getPlayer().uuid),
						Utils.unpackLongLower(getPlayer().uuid));
			} else {
				setCurrentGui(null);
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (getCurrentGui() != null) {
				setCurrentGui(null);
			} else {
				main.client.close();
			}
		}
	}

	public int getCursorBlockX() {
		return ((int) ((Main.getInputX() + renderer.camera.camerax) / World.tilesizex));
	}

	public int getCursorBlockY() {
		return ((int) ((Main.getInputY() + renderer.camera.cameray) / World.tilesizey));
	}

	public Gui getCurrentGui() {
		return currentGui;
	}

	public void setCurrentGui(Gui g) {
		if (g == null && currentGui != null) {
			currentGui.onGuiClose(renderer, this);
		}

		currentGui = g;

		if (currentGui != null) {
			currentGui.onGuiOpen(renderer, this);
		}

		Gdx.input.setCursorCatched(false);
	}

	public void openGui(String id, int x, int y) {
		setCurrentGui(GuiRegistry.instance().createNewGuiObject(id, world,
				(InventoryPlayer) getPlayer().getInventoryObject(), x, y));
	}

	@Override
	public void dispose() {
		renderer.dispose();
	}

	public PacketSwapSlot getSwapSlotPacket() {
		return swapSlot;
	}

	public int getBlockXCursor() {
		return ((int) ((Main.getInputX() + main.clientLogic.renderer.camera.camerax) / World.tilesizex));
	}

	public int getBlockYCursor() {
		return ((int) ((Main.getInputY() + main.clientLogic.renderer.camera.cameray) / World.tilesizey));
	}

}
