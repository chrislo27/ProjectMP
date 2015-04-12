package projectmp.common.world;

import projectmp.common.Main;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.packet.PacketBlockUpdate;
import projectmp.common.packet.PacketTimeUpdate;
import projectmp.server.ServerLogic;


public class ServerWorld extends World{

	ServerLogic logic;
	private PacketBlockUpdate bupacket = new PacketBlockUpdate();
	private PacketTimeUpdate timepacket = new PacketTimeUpdate();
	boolean shouldSendUpdates = true;
	
	public ServerWorld(Main main, int x, int y, boolean server, long seed, ServerLogic l) {
		super(main, x, y, server, seed);
		logic = l;
	}
	
	public void setSendingUpdates(boolean b){
		shouldSendUpdates = b;
	}
	
	@Override
	public void tickUpdate(){
		super.tickUpdate();
		if(worldTime.totalTicks % (Main.TICKS * 5) == 0){
			sendTimeUpdate();
		}
	}
	
	public void sendTimeUpdate(){
		timepacket.totalTicks = worldTime.totalTicks;
		logic.server.sendToAllTCP(timepacket);
	}

	@Override
	public void setBlock(Block b, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		blocks[x][y] = b;
		sendBlockUpdatePacket(x, y);
	}

	@Override
	public void setMeta(int m, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		meta[x][y] = m;
		sendBlockUpdatePacket(x, y);
	}
	
	private void sendBlockUpdatePacket(int x, int y){
		if(!shouldSendUpdates) return;
		bupacket.block = Blocks.instance().getKey(blocks[x][y]);
		bupacket.meta = meta[x][y];
		bupacket.x = x;
		bupacket.y = y;
		
		logic.server.sendToAllTCP(bupacket);
	}
	
}
