package projectmp.common.inventory.gui;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.world.World;


public class GuiPlayerInventory extends Gui{

	public GuiPlayerInventory(World world, InventoryPlayer player, String id, int invx, int invy) {
		super(world, player, id, invx, invy);
		addPlayerInventory();
		setUnlocalizedName("playerInv");
	}

}
