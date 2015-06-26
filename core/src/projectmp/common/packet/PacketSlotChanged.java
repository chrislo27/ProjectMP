package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.registry.GuiRegistry;
import projectmp.server.ServerLogic;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;

/**
 * sent by the server to show a slot changed, used to update other clients not the editor
 * 
 *
 */
public class PacketSlotChanged implements Packet{

	/**
	 * used to determine what inventory it's referring to
	 */
	public String invId;
	/**
	 * used to determine what inventory it's referring to
	 */
	public int invX, invY;
	
	public int slotToSwap = -1;
	
	public ItemStack changedItem = null;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		Inventory inv = GuiRegistry.instance().getInventory(invId, logic.world, invX, invY);

		if (inv == null) return;
		
		inv.setSlot(slotToSwap, changedItem);
	}

}
