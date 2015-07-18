package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.inventory.Inventory;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.tileentity.HasInventory;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketSendInventory implements Packet{

	public Inventory inv;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if(inv == null) return;
		
		HasInventory hasinv = GuiRegistry.instance().getInventory(inv.invId, logic.world, inv.invX, inv.invY);
		
		if(hasinv == null) return;
		
		hasinv.setInventoryObject(inv);
	}

}
