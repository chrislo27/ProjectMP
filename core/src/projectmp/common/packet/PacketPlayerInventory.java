package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.inventory.Inventory;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

/**
 * used to ferry player inventory updates between client and server
 * 
 *
 */
public class PacketPlayerInventory implements Packet {

	String username;
	Inventory inv;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		if (username == null) return;
		if (inv == null) return;

		if (logic.playerInventories.containsKey(username)) {
			logic.playerInventories.put(username, inv);
		}
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if (!username.equals(Main.username)) return;
		logic.playerInventory = inv;
	}

}
