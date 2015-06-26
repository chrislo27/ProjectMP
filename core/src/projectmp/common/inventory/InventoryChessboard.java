package projectmp.common.inventory;

import projectmp.common.inventory.itemstack.ItemStack;


public class InventoryChessboard extends Inventory{

	public InventoryChessboard(){
		super();
	}
	
	public InventoryChessboard(String id, int x, int y){
		super(id, x, y);
		setMaxCapacity(64);
		initSlots();
	}
	
}
