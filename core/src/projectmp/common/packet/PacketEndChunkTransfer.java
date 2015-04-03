package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketEndChunkTransfer implements Packet {

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
	}

}
