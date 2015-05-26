package projectmp.common.world;

import projectmp.common.Main;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.entity.EntityLiving;
import projectmp.common.packet.PacketBlockUpdate;
import projectmp.common.packet.PacketTimeUpdate;
import projectmp.common.packet.PacketUpdateHealth;
import projectmp.server.ServerLogic;


public class ServerWorld extends World{

	ServerLogic logic;
	private PacketBlockUpdate bupacket = new PacketBlockUpdate();
	private PacketTimeUpdate timepacket = new PacketTimeUpdate();
	private PacketUpdateHealth healthpacket = new PacketUpdateHealth();
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
		if(time.totalTicks % (Main.TICKS * 5) == 0){
			sendTimeUpdate();
		}
	}
	
	public void sendTimeUpdate(){
		timepacket.totalTicks = time.totalTicks;
		logic.server.sendToAllTCP(timepacket);
	}
	
	public void sendHealthUpdate(EntityLiving e){
		healthpacket.uuid = e.uuid;
		healthpacket.newhealth = e.health;
		logic.server.sendToAllTCP(healthpacket);
	}

	@Override
	public void setBlock(Block b, int x, int y) {
		Block old = getBlock(x, y);
		super.setBlock(b, x, y);
		if(getBlock(x, y) != old) sendBlockUpdatePacket(x, y);
	}

	@Override
	public void setMeta(int m, int x, int y) {
		int old = getMeta(x, y);
		super.setMeta(m, x, y);
		if(getMeta(x, y) != old) sendBlockUpdatePacket(x, y);
	}
	
	private void sendBlockUpdatePacket(int x, int y){
		if(!shouldSendUpdates) return;
		bupacket.block = Blocks.instance().getKey(getBlock(x, y));
		bupacket.meta = getMeta(x, y);
		bupacket.x = x;
		bupacket.y = y;
		
		logic.server.sendToAllTCP(bupacket);
	}
	
}
