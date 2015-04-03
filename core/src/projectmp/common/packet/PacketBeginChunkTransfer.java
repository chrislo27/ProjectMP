package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketBeginChunkTransfer implements Packet {

	public float percentPerChunk = 0f;
	
	public PacketBeginChunkTransfer(float percentPerChunk){
		this.percentPerChunk = percentPerChunk;
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
