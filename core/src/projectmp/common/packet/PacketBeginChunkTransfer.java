package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketBeginChunkTransfer implements Packet {

	public float percentPerChunk = 0f;
	
	public PacketBeginChunkTransfer(){
		
	}
	
	public PacketBeginChunkTransfer setPercentage(float p){
		percentPerChunk = p;
		return this;
	}
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		Main.WORLDGETTING.percentReceived = 0f;
		Main.WORLDGETTING.percentEach = percentPerChunk;
		main.setScreen(Main.WORLDGETTING);
	}

}
