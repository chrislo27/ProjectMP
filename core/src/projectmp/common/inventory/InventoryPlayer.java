package projectmp.common.inventory;


public class InventoryPlayer extends Inventory{
	
	
	
	public InventoryPlayer(){
		
	}
	
	@Override
	public InventoryPlayer setMaxCapacity(int i){
		super.setMaxCapacity(i);
		return this;
	}
	
}
