package projectmp.common.tileentity;

import projectmp.common.inventory.Inventory;

/**
 * A tile entity implements this to show their Inventory object
 * 
 */
public interface IInventory {
	
	public Inventory getInventoryObject();
	
}
