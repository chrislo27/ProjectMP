package projectmp.server;

import projectmp.common.Main;
import projectmp.common.block.Blocks;
import projectmp.common.packet.Packet1Chunk;
import projectmp.common.packet.Packet3Entities;
import projectmp.common.world.World;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;


public class ServerLogic {

	public Main main;
	public Server server;
	
	public World world = null;
	
	public int maxplayers = 1;
	
	public ServerLogic(Main m){
		main = m;
		server = main.server;
		
		world = new World(main, 32, 32);
	}
	
	public void tickUpdate(){
		world.tickUpdate();
	}
	
	public void sendEntireWorldAndEntities(Connection connection){
		for(int x = 0; x < Math.max(1, world.sizex / 16); x++){
			for(int y = 0; y < Math.max(1, world.sizey / 16); y++){
				Packet1Chunk chunk = new Packet1Chunk();
				chunk.originx = x * 16;
				chunk.originy = y * 16;
				for(int cx = 0; cx < (16) && (cx + x * 16) < world.sizex; cx++){
					for(int cy = 0; cy < (16) && (cy + y * 16) < world.sizey; cy++){
						chunk.blocks[cx][cy] = Blocks.instance().getKey(world.getBlock((cx + x * 16), (cy + y * 16)));
						chunk.meta[cx][cy] = world.getMeta((cx + x * 16), (cy + y * 16));
					}
				}
				connection.sendTCP(chunk);
			}
		}
		
		if(world.entities.size > 0){
			Packet3Entities packet = new Packet3Entities();
			packet.entities = world.entities;
			
			connection.sendTCP(packet);
		}
	}
}
