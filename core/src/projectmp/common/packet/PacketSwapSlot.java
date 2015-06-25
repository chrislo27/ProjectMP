package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.ItemStack;
import projectmp.common.registry.GuiRegistry;
import projectmp.server.ServerLogic;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;

/**
 * Client sends this to server to swap one slot of an inventory.
 * 
 *
 */
public class PacketSwapSlot implements Packet {

	public int slotToSwap = -1;
	/**
	 * this is the client's mouse stack OR the server's old "swapped" item
	 */
	public ItemStack mouseStack = null;

	/**
	 * used to determine what inventory it's referring to
	 */
	public String invId;
	/**
	 * used to determine what inventory it's referring to
	 */
	public int invX, invY;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		Inventory inv = GuiRegistry.instance().getInventory(invId, logic.world, invX, invY);

		if (inv == null) return;

		// copy the old slot
		ItemStack old = inv.getSlot(slotToSwap).copy();

		// replace the slot with the client's mouse stack
		inv.setSlot(slotToSwap, (mouseStack == null ? new ItemStack(null, 0) : mouseStack));

		// prepare a new packet to send to the client
		PacketSwapSlot packet = logic.getSwapSlotPacket();
		packet.slotToSwap = this.slotToSwap;
		packet.mouseStack = old;
		packet.invId = this.invId;
		packet.invX = this.invX;
		packet.invY = this.invY;

		logic.server.sendToTCP(connection.getID(), packet);
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		Inventory inv = GuiRegistry.instance().getInventory(invId, logic.world, invX, invY);

		if (inv == null) return;
		
		// the inventory's slot is now the mouse
		inv.setSlot(slotToSwap, logic.mouseStack.copy());
		// so set the mouse to the swapped item
		logic.mouseStack = mouseStack.copy();
		
		Gdx.input.setCursorCatched(!logic.mouseStack.isNothing());
	}

}
