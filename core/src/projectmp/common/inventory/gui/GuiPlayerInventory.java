package projectmp.common.inventory.gui;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.world.World;


public class GuiPlayerInventory extends Gui{

	public GuiPlayerInventory(World world, InventoryPlayer player) {
		super(world, player);
		addPlayerInventory();
	}

}
