package projectmp.common.registry.handler;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Gui;
import projectmp.common.tileentity.HasInventory;
import projectmp.common.world.World;


public interface IGuiHandler {
	
	public Gui getGuiObject(String id, World world, InventoryPlayer player, int x, int y);
	
	public HasInventory getInventoryHolder(String id, World world, int x, int y);
	
}
