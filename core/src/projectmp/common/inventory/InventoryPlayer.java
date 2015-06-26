package projectmp.common.inventory;

import projectmp.common.inventory.itemstack.ItemStack;


public class InventoryPlayer extends Inventory{
	
	public InventoryPlayer(){
		super();
	}
	
	public InventoryPlayer(String id, int x, int y){
		super(id, x, y);
		
		setMaxCapacity(9);
		initSlots();
		
		setSlot(0, new ItemStack("testItem", 999));
	}
	
	@Override
	public InventoryPlayer setMaxCapacity(int i){
		super.setMaxCapacity(i);
		return this;
	}
	
}
