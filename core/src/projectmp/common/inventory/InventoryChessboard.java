package projectmp.common.inventory;

import projectmp.common.inventory.itemstack.ItemStack;


public class InventoryChessboard extends Inventory{

	public InventoryChessboard(){
		setMaxCapacity(64);
		initSlots();
	}
	
}
