package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.error.InvalidPacketException;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.registry.GuiRegistry;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

/**
 * tells client to open/close a gui
 * 
 *
 */
public class PacketGuiState implements Packet {

	public boolean shouldOpen = true;
	public String guiId = null;
	public int x;
	public int y;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if (shouldOpen == false) {
			// server telling client it should be closed/doesn't exist anymore
			logic.setCurrentGui(null);
		} else {
			if (guiId == null) {
				throw new InvalidPacketException("guiId in " + this.getClass().getSimpleName()
						+ " cannot be null");
			}

			logic.setCurrentGui(GuiRegistry.instance().createNewGuiObject(guiId, logic.world,
					(InventoryPlayer) logic.getPlayer().getInventoryObject(), x, y));
		}
	}

}
