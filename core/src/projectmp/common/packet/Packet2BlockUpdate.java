package projectmp.common.packet;

import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet2BlockUpdate implements Packet {

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection) {
	}

}
