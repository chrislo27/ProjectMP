package projectmp.client;

import java.util.HashMap;

import projectmp.common.Main;
import projectmp.common.Translator;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Gui;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.packet.PacketBlockActivate;
import projectmp.common.packet.PacketItemUse;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.packet.PacketSwapSlot;
import projectmp.common.packet.PacketUpdateCursor;
import projectmp.common.packet.repository.PacketRepository;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Utils;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
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

	private boolean isUsingItem = false;
	private int lastUsingCursorX, lastUsingCursorY;

	private HashMap<String, Long> otherPlayersCursors = new HashMap<>();
	private HashMap<String, Integer> otherPlayersSelected = new HashMap<>();

	public int selectedItem = 0;

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

	public ItemStack getSelectedItem() {
		return getPlayerInventory().getSlot(selectedItem);
	}

	public InventoryPlayer getPlayerInventory() {
		EntityPlayer p = getPlayer();

		if (p == null) return null;

		return p.getInventoryObject();
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
		otherPlayersCursors.clear();
		otherPlayersSelected.clear();
	}

	public void tickUpdate() {
		renderer.tickUpdate();
		world.tickUpdate();

		if (getPlayer() != null) {
			if (main.client.isConnected()) {
				getPlayer().movementAndCollision();
				getPlayer().positionUpdate(getPlayer().x, getPlayer().y);

				if (isUsingItem && !getSelectedItem().isNothing()) {

					if (MathHelper.calcDistance(getPlayer().visualX, getPlayer().visualY,
							getCursorBlockX(), getCursorBlockY()) > getSelectedItem().getItem()
							.getRange()) {
						stopUsingItem();
					} else {
						getSelectedItem().getItem().onUsing(world, getPlayer(), selectedItem,
								getCursorBlockX(), getCursorBlockY());

						if (getCursorBlockX() != lastUsingCursorX
								|| getCursorBlockY() != lastUsingCursorY) {
							lastUsingCursorX = getCursorBlockX();
							lastUsingCursorY = getCursorBlockY();

							PacketUpdateCursor packet = PacketRepository.instance().updateCursor;
							packet.x = lastUsingCursorX;
							packet.y = lastUsingCursorY;
							client.sendTCP(packet);
						}
					}
				}

				// send a movement update if the player moved last tick OR if it's time to send a packet
				if (getPlayer().hasMovedLastTick()
						|| (world.time.totalTicks
								% ((int) (Main.TICKS * TIME_BETWEEN_FORCE_UPDATE)) == 0)) {
					prepareMovementUpdate();
					if ((world.time.totalTicks % ((int) (Main.TICKS * TIME_BETWEEN_FORCE_UPDATE)) == 0)) {
						main.client.sendTCP(playerUpdate);
					} else {
						main.client.sendUDP(playerUpdate);
					}
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
		BitmapFont font = main.font;
		
		main.font.draw(main.batch, "latency: " + main.client.getReturnTripTime() + " ms", 5,
				Main.convertY(starting));
		if (world != null) {
			main.font.draw(main.batch, "entities: " + world.getNumberOfEntities(), 5,
					Main.convertY(starting + font.getCapHeight()));
			main.font.draw(main.batch,
					"lightingTimeTaken: " + (world.lightingEngine.getLastUpdateLength() / 1000000f)
							+ " ms", 5, Main.convertY(starting + font.getCapHeight() * 2));
			main.font.draw(main.batch, "worldTime: " + world.time.currentDayTicks
					+ ", lastDayBri: " + world.lightingEngine.lastDayBrightness, 5,
					Main.convertY(starting + font.getCapHeight() * 3));
			main.font.draw(main.batch, "timeOfDay: " + world.time.getCurrentTimeOfDay(), 5,
					Main.convertY(starting + font.getCapHeight() * 4));
			main.font.draw(main.batch, "x: " + getPlayer().x, 5, Main.convertY(starting + font.getCapHeight() * 5));
			main.font.draw(main.batch, "y: " + getPlayer().y, 5, Main.convertY(starting + font.getCapHeight()* 6));
			main.font.draw(main.batch, "cursorx: " + getCursorBlockX(), 5,
					Main.convertY(starting + font.getCapHeight() * 7));
			main.font.draw(main.batch, "cursory: " + getCursorBlockY(), 5,
					Main.convertY(starting + font.getCapHeight() * 8));
			main.font.draw(
					main.batch,
					"light: "
							+ String.format("%.3f", world.lightingEngine.getActualLighting(
									getCursorBlockX(), getCursorBlockY()))
							+ ", b: "
							+ world.lightingEngine.getBrightness(getCursorBlockX(),
									getCursorBlockY())
							+ ", s: "
							+ String.format("%.3f", world.lightingEngine
									.getSkyLightFromTOD(world.lightingEngine.getSkyLight(
											getCursorBlockX(), getCursorBlockY()))), 5, Main
							.convertY(starting + font.getCapHeight() * 9));
			main.font.draw(main.batch, "weather: "
					+ (world.getWeather() == null ? null : world.getWeather().getClass()
							.getSimpleName()
							+ ", " + world.getWeather().getTimeRemaining() + " ticks left"), 5,
					Main.convertY(starting + font.getCapHeight() * 10));
			main.font.draw(main.batch, "cam x: " + renderer.camera.camerax, 5,
					Main.convertY(starting + font.getCapHeight() * 11));
			main.font.draw(main.batch, "cam y: " + renderer.camera.camerax, 5,
					Main.convertY(starting + font.getCapHeight() * 12));
			main.font.draw(main.batch, "cam wantedx: " + renderer.camera.wantedx, 5,
					Main.convertY(starting + font.getCapHeight() * 13));
			main.font.draw(main.batch, "cam wantedy: " + renderer.camera.wantedx, 5,
					Main.convertY(starting + font.getCapHeight() * 14));
			main.font.draw(main.batch, "visualX: " + getPlayer().visualX, 5,
					Main.convertY(starting + font.getCapHeight() * 15));
			main.font.draw(main.batch, "visualY: " + getPlayer().visualY, 5,
					Main.convertY(starting + font.getCapHeight() * 16));
			main.font.draw(main.batch, "lerpVeloX: " + getPlayer().lerpVeloX, 5,
					Main.convertY(starting + font.getCapHeight() * 17));
			main.font.draw(main.batch, "lerpVeloY: " + getPlayer().lerpVeloY, 5,
					Main.convertY(starting + font.getCapHeight() * 18));
			main.font
					.draw(main.batch,
							"breakingProgress: "
									+ String.format("%.3f", world.getBreakingProgress(
											getCursorBlockX(), getCursorBlockY())), 5, Main
									.convertY(starting + font.getCapHeight() * 19));
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

	public void playerInput() {
		if (getPlayer() == null || !main.client.isConnected()) return;

		if (Gdx.input.isKeyPressed(Keys.A)) {
			getPlayer().moveLeft(false);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			getPlayer().moveRight(false);
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			getPlayer().jump();
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {

		}

		if (getCurrentGui() == null) {
			int x = getCursorBlockX();
			int y = getCursorBlockY();

			if (Gdx.input.isButtonPressed(Buttons.LEFT)) { // item use
				if (!isUsingItem) useItem();
			} else if (Gdx.input.isButtonPressed(Buttons.LEFT) == false) {
				stopUsingItem();
			}

			if (Utils.isButtonJustPressed(Buttons.RIGHT) && !isUsingItem) { // block activate
				world.getBlock(x, y).onActivate(world, x, y, getPlayer());
				// send packet to server
				PacketBlockActivate packet = PacketRepository.instance().blockActivate;
				packet.playerUsername = Main.username;
				packet.blockX = x;
				packet.blockY = y;

				client.sendTCP(packet);
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
	
	public boolean onScrolled(int amount){
		if(getCurrentGui() == null){
			selectedItem += (int) (Math.signum(amount));
			if(selectedItem < 0) selectedItem = 8;
			if(selectedItem > 8) selectedItem = 0;
		}
		
		return getCurrentGui() == null;
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

	public boolean isUsingItem() {
		return isUsingItem;
	}

	public void useItem() {
		if (!getSelectedItem().isNothing()) {
			if (MathHelper.calcDistance(getPlayer().visualX, getPlayer().visualY,
					getCursorBlockX(), getCursorBlockY()) > getSelectedItem().getItem().getRange()) return;

			if (!isUsingItem) {
				isUsingItem = true;
				getSelectedItem().getItem().onUseStart(world, getPlayer(), selectedItem,
						getCursorBlockX(), getCursorBlockY());

				renderer.batch.begin();
				getSelectedItem().getItem().onUseStartRender(renderer, getPlayer(), selectedItem,
						getCursorBlockX(), getCursorBlockY());
				renderer.batch.end();

				PacketItemUse packet = PacketRepository.instance().itemUse;
				packet.status = PacketItemUse.ON_START;
				packet.selectedSlot = selectedItem;
				packet.cursorX = getCursorBlockX();
				packet.cursorY = getCursorBlockY();
				client.sendTCP(packet);

				lastUsingCursorX = getCursorBlockX();
				lastUsingCursorY = getCursorBlockY();

				PacketUpdateCursor packet2 = PacketRepository.instance().updateCursor;
				packet2.x = lastUsingCursorX;
				packet2.y = lastUsingCursorY;
				packet2.username = Main.username;
				client.sendTCP(packet);
			}
		}
	}

	public void stopUsingItem() {
		if (!getSelectedItem().isNothing()) {
			if (isUsingItem) {
				isUsingItem = false;
				getSelectedItem().getItem().onUseEnd(world, getPlayer(), selectedItem,
						getCursorBlockX(), getCursorBlockY());

				renderer.batch.begin();
				getSelectedItem().getItem().onUseEndRender(renderer, getPlayer(), selectedItem,
						getCursorBlockX(), getCursorBlockY());
				renderer.batch.end();

				PacketItemUse packet = PacketRepository.instance().itemUse;
				packet.status = PacketItemUse.ON_END;
				packet.selectedSlot = selectedItem;
				packet.cursorX = getCursorBlockX();
				packet.cursorY = getCursorBlockY();
				client.sendTCP(packet);

				PacketUpdateCursor packet2 = PacketRepository.instance().updateCursor;
				packet2.x = lastUsingCursorX;
				packet2.y = lastUsingCursorY;
				client.sendTCP(packet);
			}
		}
	}

	public long getOtherPlayerCursor(String username) {
		if (!otherPlayersCursors.containsKey(username)) {
			otherPlayersCursors.put(username, 0L);
		}

		return otherPlayersCursors.get(username);
	}

	public void putOtherPlayerCursor(String username, long cursor) {
		otherPlayersCursors.put(username, cursor);
	}

	public void removeOtherPlayerCursor(String username) {
		otherPlayersCursors.remove(username);
	}

	public void putOtherPlayerUsing(String username, int slot) {
		otherPlayersSelected.put(username, slot);
	}

	public void removeOtherPlayerUsingItem(String username) {
		if (otherPlayersSelected.remove(username) != null) {

		}
	}

	public HashMap<String, Integer> getOtherPlayersSelected() {
		return otherPlayersSelected;
	}

}
