package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketBeginChunkTransfer implements Packet {

	public float percentPerChunk = 0f;
	public boolean isSingleplayer = false;

	public PacketBeginChunkTransfer() {

	}

	public PacketBeginChunkTransfer setPercentage(float p) {
		percentPerChunk = p;
		return this;
	}
	
	public PacketBeginChunkTransfer setSingleplayer(boolean b){
		isSingleplayer = b;
		return this;
	}

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		Main.WORLDGETTING.percentReceived = 0f;
		Main.WORLDGETTING.percentEach = percentPerChunk;
		Main.WORLDGETTING.isSingleplayer = isSingleplayer;
		logic.main.setScreen(Main.WORLDGETTING);
	}

}
