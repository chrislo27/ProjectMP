package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.inventory.Inventory;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketSendInv implements Packet {

	Inventory inv = null;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
	}

}
