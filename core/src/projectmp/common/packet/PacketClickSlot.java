package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketClickSlot implements Packet{

	public String guiId = null;
	/**
	 * The slot in the GuiHandler
	 */
	public int slotId = -999; // -1 is reserved for mouse cursor
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
	}

}
