package projectmp.common.inventory;

import projectmp.common.item.Item;


public class ItemStack {
	
	Item item;
	int quantity = 1;
	
	public ItemStack(Item i, int amount){
		item = i;
		quantity = amount;
	}
	
}
