package projectmp.common.inventory;


public class InventoryPlayer extends Inventory{

	ItemStack mouseStack = new ItemStack(null, 0);
	
	public InventoryPlayer(){
		
	}
	
	@Override
	public InventoryPlayer setMaxCapacity(int i){
		super.setMaxCapacity(i);
		return this;
	}
	
}
