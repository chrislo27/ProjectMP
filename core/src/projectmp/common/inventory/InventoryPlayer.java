package projectmp.common.inventory;


public class InventoryPlayer extends Inventory{
	
	
	
	public InventoryPlayer(){
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
