package projectmp.server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import projectmp.common.Main;
import projectmp.common.block.Blocks;
import projectmp.common.chunk.Chunk;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.generation.GenerationGroups;
import projectmp.common.io.WorldNBTIO;
import projectmp.common.io.WorldSavingLoading;
import projectmp.common.packet.PacketBeginChunkTransfer;
import projectmp.common.packet.PacketEntities;
import projectmp.common.packet.PacketGuiState;
import projectmp.common.packet.PacketPositionUpdate;
import projectmp.common.packet.PacketSendChunk;
import projectmp.common.packet.PacketSendInventory;
import projectmp.common.packet.PacketSlotChanged;
import projectmp.common.packet.repository.PacketRepository;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.util.FileNameUtils;
import projectmp.common.world.ServerWorld;
import projectmp.server.player.ServerPlayer;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class ServerLogic {

	private static final float TIME_BETWEEN_FORCE_SEND = 5;
	
	public boolean isSingleplayer = false;

	public Main main;
	public Server server;

	public ServerWorld world = null;

	public int maxplayers = 2;

	private Array<ServerPlayer> players = new Array<>();

	public ServerLogic(Main m) {
		main = m;
		server = main.server;

		world = new ServerWorld(main, 1024, 512, System.nanoTime(), this);
		new Thread() {

			@Override
			public void run() {
				long ms = System.currentTimeMillis();
				Main.logger.debug("beginning generation");
				world.setSendingUpdates(false);
				GenerationGroups.generateWorld(world);
				world.setSendingUpdates(true);
				Main.logger.debug("finished generating; took " + (System.currentTimeMillis() - ms)
						+ " ms");

				//				try {
				//					byte[] worldBytes = WorldSavingLoading.loadWorld(new File("saves/save0/world.dat"));
				//					world = (ServerWorld) WorldNBTIO.decode(world, worldBytes);
				//				} catch (IOException e) {
				//					e.printStackTrace();
				//				}

				try {
					save(FileNameUtils.DEFAULT_SAVE_FOLDER + "save0/");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	public void tickUpdate() {
		world.tickUpdate();

		for (int i = 0; i < players.size; i++) {
			ServerPlayer sp = players.get(i);

			sp.tickUpdate(this);
		}

		if (server.getConnections().length > 0 && world.getNumberOfEntities() > 0) {
			PacketPositionUpdate positionUpdate = PacketRepository.instance().positionUpdate;

			if (positionUpdate.entityid.length < world.getNumberOfEntities()
					|| Math.abs(positionUpdate.entityid.length - world.getNumberOfEntities()) >= 32) {
				positionUpdate.resetTables(world.getNumberOfEntities());
			}

			boolean shouldSend = false;
			int iter = 0;
			boolean force = ((int) (main.totalSeconds * Main.TICKS)) % ((int) (TIME_BETWEEN_FORCE_SEND * Main.TICKS)) == 0;
			
			for (int i = 0; i < world.getNumberOfEntities(); i++) {
				Entity e = world.getEntityByIndex(i);

				if (!e.hasMovedLastTick() && !force) continue;
				positionUpdate.entityid[iter] = e.uuid;
				positionUpdate.x[iter] = e.x;
				positionUpdate.y[iter] = e.y;
				positionUpdate.velox[iter] = e.velox;
				positionUpdate.veloy[iter] = e.veloy;
				shouldSend = true;

				iter++;
			}

			if (shouldSend && !force){
				server.sendToAllUDP(positionUpdate);
			}else if(force){
				server.sendToAllTCP(positionUpdate);
			}
		}
	}

	public void save(String folder) throws IOException {
		new File(folder).mkdirs();

		File f = new File(folder + FileNameUtils.WORLD_FILE_NAME + FileNameUtils.FILE_EXTENSION);
		f.createNewFile();

		byte[] worldBytes = WorldNBTIO.encodeWorld(world);
		WorldSavingLoading.saveBytes(WorldNBTIO.encodeWorld(world), f);

		f = new File(folder + FileNameUtils.PLAYER_FILE_NAME + FileNameUtils.FILE_EXTENSION);
		f.createNewFile();

		byte[] playerBytes = WorldNBTIO.encodePlayers(players);
		WorldSavingLoading.saveBytes(playerBytes, f);
	}

	public void sendEntireWorld(Connection connection) {
		Array<PacketSendChunk> queue = new Array<PacketSendChunk>(Math.max(1, world.sizex / 16)
				+ Math.max(1, world.sizey / 16));

		Array<String> blockKeys = new Array<>();
		HashMap<String, Integer> blockMap = new HashMap<>();

		for (int x = 0; x < world.getWidthInChunks(); x++) {
			for (int y = 0; y < world.getHeightInChunks(); y++) {
				blockKeys.clear();
				blockMap.clear();

				PacketSendChunk packet = new PacketSendChunk();

				packet.originx = x * Chunk.CHUNK_SIZE;
				packet.originy = y * Chunk.CHUNK_SIZE;

				for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
					for (int k = 0; k < Chunk.CHUNK_SIZE; k++) {
						Chunk currentChunk = world.getChunk(x, y);

						String blockKey = Blocks.instance()
								.getKey(currentChunk.getChunkBlock(j, k));

						if (!blockKeys.contains(blockKey, false)) {
							blockKeys.add(blockKey);
							blockMap.put(blockKey, blockKeys.size - 1);
						}

						packet.blocks[j][k] = blockMap.get(blockKey);
						packet.meta[j][k] = currentChunk.getChunkMeta(j, k);
						packet.tileEntities[j][k] = currentChunk.getChunkTileEntity(j, k);
					}
				}

				packet.blockKeys = blockKeys.toArray(String.class);

				queue.add(packet);
			}
		}

		connection.sendTCP(new PacketBeginChunkTransfer().setPercentage(1.0f / queue.size)
				.setSingleplayer(isSingleplayer));

		connection.addListener(new ChunkQueueSender(queue, connection));
	}

	public void sendEntities(Connection connection) {
		if (world.getNumberOfEntities() > 0) {
			PacketEntities packet = new PacketEntities();
			packet.entities = new Entity[world.getNumberOfEntities()];
			for (int i = 0; i < packet.entities.length; i++) {
				packet.entities[i] = world.getEntityByIndex(i);
			}

			connection.sendTCP(packet);
		}
	}

	public int getConnectionIDByName(String name) {
		for (int i = 0; i < server.getConnections().length; i++) {
			if (server.getConnections()[i].toString().equals(name)) return server.getConnections()[i]
					.getID();
		}
		return -1;
	}

	public String getConnectionNameByID(int id) {
		for (int i = 0; i < server.getConnections().length; i++) {
			if (server.getConnections()[i].getID() == id) return server.getConnections()[i]
					.toString();
		}
		return null;
	}

	public EntityPlayer getPlayerByName(String name) {
		for (int i = 0; i < world.getNumberOfEntities(); i++) {
			Entity e = world.getEntityByIndex(i);
			if (e instanceof EntityPlayer) {
				if (((EntityPlayer) e).username.equals(name)) {
					return (EntityPlayer) e;
				}
			}
		}

		return null;
	}

	/**
	 * Updates the ServerPlayer, removes the entity, and saves the world.
	 * @param name
	 */
	protected void removePlayer(String name) {
		EntityPlayer p = getPlayerByName(name);

		if (p != null) {
			ServerPlayer sp = getServerPlayerByName(name);
			if (sp != null) getServerPlayerByName(name).stopUsingItem(this, sp.getCursorX(),
					sp.getCursorY());
			updatePlayerData(name, p);
			world.removeEntity(p.uuid);
			try {
				save(FileNameUtils.DEFAULT_SAVE_FOLDER + "save0/");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Used to update the ServerPlayer instance with entity data before disconnecting
	 * <br>
	 * This method actually deletes the existing instance and creates a new one. The method that creates the new instance updates the fields correctly.
	 * @param p
	 */
	public void updatePlayerData(String username, EntityPlayer p) {
		updatePlayerData(username, createNewServerPlayer(p));
	}

	/**
	 * Used to update the ServerPlayer instance with entity data before disconnecting
	 * <br>
	 * This method actually deletes the existing instance and creates a new one. The method that creates the new instance updates the fields correctly.
	 * @param p
	 */
	public void updatePlayerData(String username, ServerPlayer sp) {
		for (int i = players.size - 1; i >= 0; i--) {
			if (players.get(i).username.equals(username)) {
				players.removeIndex(i);
			}
		}

		players.add(sp);
	}

	/**
	 * Returns a brand new server player instance
	 * @param p
	 * @return
	 */
	public ServerPlayer createNewServerPlayer(EntityPlayer p) {
		ServerPlayer sp = new ServerPlayer(p.username, p.uuid);

		sp.posx = p.x;
		sp.posy = p.y;

		return sp;
	}

	public ServerPlayer getServerPlayerByName(String username) {
		for (int i = 0; i < players.size; i++) {
			ServerPlayer p = players.get(i);

			if (p.username.equals(username)) return p;
		}

		return null;
	}

	public void sendGuiState(EntityPlayer player, String guiId, int x, int y, boolean shouldOpen) {
		PacketGuiState guiStatePacket = PacketRepository.instance().guiState;

		guiStatePacket.guiId = guiId;
		guiStatePacket.shouldOpen = shouldOpen;
		guiStatePacket.x = x;
		guiStatePacket.y = y;

		server.sendToTCP(getConnectionIDByName(player.username), guiStatePacket);
	}

	public void openGui(EntityPlayer player, String guiId, int x, int y) {
		sendGuiState(player, guiId, x, y, true);
	}

	public void closeGui(EntityPlayer player, String guiId, int x, int y) {
		sendGuiState(player, guiId, x, y, false);
	}

	/**
	 * Updates all the clients with the changed slot in the inventory.
	 * @param invId
	 * @param invX
	 * @param invY
	 * @param newSlot
	 */
	public void updateClientsOfInventorySlotChange(String invId, int invX, int invY, int newSlot) {
		PacketSlotChanged changed = PacketRepository.instance().slotChanged;
		changed.changedItem = GuiRegistry.instance().getInventory(invId, world, invX, invY)
				.getInventoryObject().getSlot(newSlot);
		changed.slotToSwap = newSlot;
		changed.invId = invId;
		changed.invX = invX;
		changed.invY = invY;

		server.sendToAllTCP(changed);
	}
	
	/**
	 * Updates all the clients by re-sending the inventory object
	 * @param invId
	 * @param invX
	 * @param invY
	 */
	public void updateClientsOfTotalInventoryChange(String invId, int invX, int invY){
		PacketSendInventory packet = PacketRepository.instance().sendInv;
		
		packet.inv = GuiRegistry.instance().getInventory(invId, world, invX, invY).getInventoryObject();
		
		server.sendToAllTCP(packet);
	}

}
