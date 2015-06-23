package projectmp.common.registry;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Gui;
import projectmp.common.world.World;


public interface IGuiHandler {
	
	public Gui getGuiObject(String id, World world, InventoryPlayer player, int x, int y);
	
}
