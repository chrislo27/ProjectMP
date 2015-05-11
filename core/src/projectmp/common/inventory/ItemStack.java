package projectmp.common.inventory;

import projectmp.common.item.Item;


public class ItemStack {
	
	String item;
	int quantity = 1;
	
	public ItemStack(String i, int amount){
		item = i;
		quantity = amount;
	}
	
}
