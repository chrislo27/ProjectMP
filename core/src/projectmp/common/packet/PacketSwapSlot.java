package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.item.Items;
import projectmp.common.packet.repository.PacketRepository;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.util.Utils;
import projectmp.common.util.annotation.sidedictation.Side;
import projectmp.common.util.annotation.sidedictation.SideOnly;
import projectmp.server.ServerLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.esotericsoftware.kryonet.Connection;

/**
 * Client sends this to server to swap one slot of an inventory.
 * 
 *
 */
public class PacketSwapSlot extends PacketSlotChanged {

	/**
	 * this is the client's mouse stack OR the server's old "swapped" item
	 */
	public ItemStack mouseStack = null;

	@SideOnly(Side.SERVER)
	public int buttonUsed = Buttons.LEFT;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		Inventory inv = GuiRegistry.instance().getInventory(invId, logic.world, invX, invY);

		if (inv == null) return;

		if (buttonUsed == Buttons.LEFT) {
			leftClickFunctionality(connection, logic, inv);
		} else if (buttonUsed == Buttons.RIGHT) {
			rightClickFunctionality(connection, logic, inv);
		}
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		Inventory inv = GuiRegistry.instance().getInventory(invId, logic.world, invX, invY);

		if (inv == null) return;

		// set the mouse stack
		logic.mouseStack = mouseStack.copy();

		Utils.setCursorVisibility(logic.mouseStack.isNothing());
	}

	@SideOnly(Side.SERVER)
	private void leftClickFunctionality(Connection connection, ServerLogic logic, Inventory inv) {
		ItemStack inventorySlot = inv.getSlot(slotToSwap);
		ItemStack newMouseStack = mouseStack; // this should never retain

		if (!inventorySlot.equalsIgnoreAmount(mouseStack)) {
			// if they differ, totally swap the contents

			// copy the old slot which will now be the mouse stack
			newMouseStack = inventorySlot.copy();

			// replace the slot with the client's mouse stack
			inv.setSlot(slotToSwap, mouseStack);

			// resetting inventorySlot is needed to update correctly
			inventorySlot = inv.getSlot(slotToSwap);
		} else {
			// they're the same, so try to stack it together as much as possible

			if(inventorySlot.getItem() == null){
				// Main.logger.warn("Unknown item: " + inventorySlot.getItemString());
				return;
			}
			
			// get how much we can put/shove inside the slot
			int shoveableRemaining = inventorySlot.getItem().getMaxStack()
					- inventorySlot.getAmount();

			// we can put the rest of the mouse stack in the slot, easy
			if (mouseStack.getAmount() <= shoveableRemaining) {
				inventorySlot.setAmount(inventorySlot.getAmount() + mouseStack.getAmount());

				mouseStack.setAmount(0);
				mouseStack.setItem(null);
			} else {
				// there's too much in the mouse so transfer as much as possible

				inventorySlot.setAmount(inventorySlot.getItem().getMaxStack());

				mouseStack.setAmount(mouseStack.getAmount() - shoveableRemaining);
			}

			newMouseStack = mouseStack;
		}

		updateMouseStack(logic, connection, newMouseStack);
		updateOtherClients(logic, inventorySlot);
	}

	@SideOnly(Side.SERVER)
	private void rightClickFunctionality(Connection connection, ServerLogic logic, Inventory inv) {
		ItemStack inventorySlot = inv.getSlot(slotToSwap);

		// take half of the slot
		if (mouseStack.isNothing() && !inventorySlot.isNothing()) {
			int originalAmount = inventorySlot.getAmount();

			mouseStack.setItem(inventorySlot.getItemString());
			mouseStack.setAmount((originalAmount / 2) + (originalAmount % 2)); // take the bigger half if applicable

			inventorySlot.setAmount(originalAmount / 2); // takes the smaller half
			if(inventorySlot.isNothing()){
				inventorySlot.setItem(null);
			}
		} else if (!inventorySlot.isNothing() && !inventorySlot.equalsIgnoreAmount(mouseStack)) { // right clicking over different item; simply replace like left click
			// replace like left click
			leftClickFunctionality(connection, logic, inv);
			return;
		} else if (inventorySlot.isNothing() || inventorySlot.equalsIgnoreAmount(mouseStack)) { // the slot is empty/same, add one to it
			// check if the item capacity is maxed out
			if (!inventorySlot.isNothing() && inventorySlot.equalsIgnoreAmount(mouseStack)) {
				if (inventorySlot.getAmount() >= inventorySlot.getItem().getMaxStack()) return;
			}

			// add one to the slot
			inventorySlot.setItem(mouseStack.getItemString()); // this takes care of nothing slot
			inventorySlot.setAmount(inventorySlot.getAmount() + 1);

			// remove one from mouse stack
			mouseStack.setAmount(mouseStack.getAmount() - 1);

			// if mouse amt is 0 set to item to null
			if (mouseStack.isNothing()) {
				mouseStack.setItem(null);
			}
		}

		updateMouseStack(logic, connection, mouseStack);
		updateOtherClients(logic, inventorySlot);
	}

	@SideOnly(Side.SERVER)
	private void updateOtherClients(ServerLogic logic, ItemStack changedItem) {
		// update the other clients of the change
		PacketSlotChanged changed = PacketRepository.instance().slotChanged;
		changed.changedItem = changedItem;
		changed.slotToSwap = slotToSwap;
		changed.invId = invId;
		changed.invX = invX;
		changed.invY = invY;

		logic.server.sendToAllTCP(changed);
	}

	@SideOnly(Side.SERVER)
	private void updateMouseStack(ServerLogic logic, Connection connection, ItemStack mouseStack) {
		// prepare a packet to send to the client to refresh the mouse stack
		PacketSwapSlot packet = PacketRepository.instance().swapSlot;
		packet.slotToSwap = this.slotToSwap;
		packet.mouseStack = mouseStack;
		packet.invId = this.invId;
		packet.invX = this.invX;
		packet.invY = this.invY;

		logic.server.sendToTCP(connection.getID(), packet);
	}

}
