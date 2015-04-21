package projectmp.server;

import projectmp.common.Main;
import projectmp.common.block.Blocks;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.packet.PacketBeginChunkTransfer;
import projectmp.common.packet.PacketEntities;
import projectmp.common.packet.PacketNewEntity;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.packet.PacketPositionUpdate;
import projectmp.common.packet.PacketRemoveEntity;
import projectmp.common.packet.PacketSendChunk;
import projectmp.common.world.ServerWorld;
import projectmp.server.networking.ChunkQueueSender;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class ServerLogic {

	public Main main;
	public Server server;

	public ServerWorld world = null;

	public int maxplayers = 2;

	private PacketPositionUpdate positionUpdate = new PacketPositionUpdate();
	private PacketPlayerPosUpdate updatePlayer = new PacketPlayerPosUpdate();
	private PacketRemoveEntity removeEntity = new PacketRemoveEntity();
	private PacketNewEntity newEntity = new PacketNewEntity();

	public ServerLogic(Main m) {
		main = m;
		server = main.server;

		world = new ServerWorld(main, 1028, 512, true, System.nanoTime(), this);
		new Thread(){
			
			@Override
			public void run(){
				long ms = System.currentTimeMillis();
				Main.logger.debug("beginning generation");
				world.setSendingUpdates(false);
				world.generate();
				world.setSendingUpdates(true);
				Main.logger.debug("finished generating; took " + (System.currentTimeMillis() - ms) + " ms");
				world.lightingEngine.updateLighting(0, 0, world.sizex, world.sizey);
				Main.logger.debug("Lighting update for entire world on init took "
						+ (world.lightingEngine.getLastUpdateLength() / 1000000f) + " ms");
			}
			
		}.start();
	}

	public void tickUpdate() {
		world.tickUpdate();

		if (server.getConnections().length > 0 && world.entities.size > 0) {
			if(positionUpdate.entityid.length < world.entities.size || Math.abs(positionUpdate.entityid.length - world.entities.size) >= 32){
				positionUpdate.entityid = new long[world.entities.size];
				positionUpdate.x = new float[world.entities.size];
				positionUpdate.y = new float[world.entities.size];
			}
			
			boolean shouldSend = false;
			int iter = 0;
			for (Entity e : world.entities) {
				if (!e.hasMovedLastTick()) continue;
				positionUpdate.entityid[iter] = e.uuid;
				positionUpdate.x[iter] = e.x;
				positionUpdate.y[iter] = e.y;
				shouldSend = true;

				iter++;
			}
			
			if(shouldSend) server.sendToAllUDP(positionUpdate);
		}
	}

	public void sendEntireWorld(Connection connection) {
		Array<PacketSendChunk> queue = new Array<PacketSendChunk>(Math.max(1, world.sizex / 16) + Math.max(1, world.sizey / 16));
		
		for (int x = 0; x < Math.max(1, world.sizex / 16); x++) {
			for (int y = 0; y < Math.max(1, world.sizey / 16); y++) {
				PacketSendChunk chunk = new PacketSendChunk();
				chunk.originx = x * 16;
				chunk.originy = y * 16;
				for (int cx = 0; cx < (16) && (cx + x * 16) < world.sizex; cx++) {
					for (int cy = 0; cy < (16) && (cy + y * 16) < world.sizey; cy++) {
						chunk.blocks[cx][cy] = Blocks.instance().getKey(
								world.getBlock((cx + x * 16), (cy + y * 16)));
						chunk.meta[cx][cy] = world.getMeta((cx + x * 16), (cy + y * 16));
					}
				}

				queue.add(chunk);
			}
		}
		
		connection.sendTCP(new PacketBeginChunkTransfer().setPercentage(1.0f / queue.size));
		
		connection.addListener(new ChunkQueueSender(queue, connection));
	}
	
	public void sendEntities(Connection connection){
		if (world.entities.size > 0) {
			PacketEntities packet = new PacketEntities();
			packet.entities = new Entity[world.entities.size];
			for (int i = 0; i < packet.entities.length; i++) {
				packet.entities[i] = world.entities.get(i);
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
		for (int i = 0; i < world.entities.size; i++) {
			Entity e = world.entities.get(i);
			if (e instanceof EntityPlayer) {
				if (((EntityPlayer) e).username.equals(name)) {
					return (EntityPlayer) e;
				}
			}
		}

		return null;
	}
	
	protected void removePlayer(String name){
		EntityPlayer p = getPlayerByName(name);
		
		if(p != null){
			removeEntity.uuid = p.uuid;
			server.sendToAllTCP(removeEntity);
			Main.logger.debug("Removed player " + p.username + " on server");
			
			world.entities.removeValue(p, false);
		}
	}
	
}
