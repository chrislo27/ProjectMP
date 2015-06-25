package projectmp.common.inventory.gui;

import projectmp.common.inventory.InventoryPlayer;


public class GuiPlayerInventory extends Gui{

	public GuiPlayerInventory(InventoryPlayer player, String id, int invx, int invy) {
		super(player, id, invx, invy);
		addPlayerInventory();
	}

}
