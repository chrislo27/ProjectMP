package projectmp.common.tileentity;

import projectmp.common.inventory.Inventory;

/**
 * A tile entity or normal entity (or anything for that matter) implements this to show they have an inventory object.
 * 
 */
public interface HasInventory {
	
	public Inventory getInventoryObject();
	
}
