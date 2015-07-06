package projectmp.common.inventory;

import projectmp.common.inventory.itemstack.ItemStack;


public class InventoryPlayer extends Inventory{
	
	public static final int MAX_INV_CAPACITY = 36;
	
	public InventoryPlayer(){
		super();
	}
	
	public InventoryPlayer(String id, int x, int y){
		super(id, x, y);
		
		setMaxCapacity(MAX_INV_CAPACITY);
		initSlots();
	}
	
	@Override
	public InventoryPlayer setMaxCapacity(int i){
		super.setMaxCapacity(i);
		return this;
	}
	
}
