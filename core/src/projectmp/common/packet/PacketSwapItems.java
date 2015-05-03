package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketSwapItems implements Packet {

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
	}

}
