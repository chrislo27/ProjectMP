package projectmp.server;

import projectmp.common.Main;
import projectmp.common.block.Blocks;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.entity.EntitySquare;
import projectmp.common.packet.Packet1Chunk;
import projectmp.common.packet.Packet3Entities;
import projectmp.common.packet.Packet4PositionUpdate;
import projectmp.common.packet.Packet7NewEntity;
import projectmp.common.packet.Packet8RemoveEntity;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class ServerLogic {

	public Main main;
	public Server server;

	public World world = null;

	public int maxplayers = 1;

	private Packet4PositionUpdate positionUpdate = new Packet4PositionUpdate();
	private Packet8RemoveEntity removeEntity = new Packet8RemoveEntity();
	private Packet7NewEntity newEntity = new Packet7NewEntity();

	public ServerLogic(Main m) {
		main = m;
		server = main.server;

		world = new World(main, 16, 16, true);
	}

	public void tickUpdate() {
		world.tickUpdate();

		if (server.getConnections().length > 0) {
			for (Entity e : world.entities) {
				if (e.lastTickX == e.x && e.lastTickY == e.y) continue;
				if(e instanceof EntityPlayer) continue;
				positionUpdate.entityid = e.uuid;
				positionUpdate.x = e.x;
				positionUpdate.y = e.y;

				server.sendToAllUDP(positionUpdate);
			}
		}
	}

	public void sendEntireWorld(Connection connection) {
		for (int x = 0; x < Math.max(1, world.sizex / 16); x++) {
			for (int y = 0; y < Math.max(1, world.sizey / 16); y++) {
				Packet1Chunk chunk = new Packet1Chunk();
				chunk.originx = x * 16;
				chunk.originy = y * 16;
				for (int cx = 0; cx < (16) && (cx + x * 16) < world.sizex; cx++) {
					for (int cy = 0; cy < (16) && (cy + y * 16) < world.sizey; cy++) {
						chunk.blocks[cx][cy] = Blocks.instance().getKey(
								world.getBlock((cx + x * 16), (cy + y * 16)));
						chunk.meta[cx][cy] = world.getMeta((cx + x * 16), (cy + y * 16));
					}
				}
				connection.sendTCP(chunk);
			}
		}
	}
	
	public void sendEntities(Connection connection){
		if (world.entities.size > 0) {
			Packet3Entities packet = new Packet3Entities();
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
	
	public Packet4PositionUpdate getSharedPosUpdatePacket(){
		return positionUpdate;
	}
	
	protected void removePlayer(int connectionID){
		EntityPlayer p = getPlayerByName(getConnectionNameByID(connectionID));
		
		if(p != null){
			removeEntity.uuid = p.uuid;
			server.sendToAllExceptTCP(connectionID, removeEntity);
			
			world.entities.removeValue(p, false);
		}
	}
	
}
