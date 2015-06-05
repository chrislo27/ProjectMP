package projectmp.common.inventory;

import projectmp.common.item.Item;
import projectmp.common.item.Items;


public class ItemStack {
	
	String item;
	int quantity = 1;
	
	public ItemStack(String i, int amount){
		item = i;
		quantity = amount;
	}
	
	/**
	 * Convienence method to get the item from the stored string
	 * @return
	 */
	public Item getItem(){
		return Items.instance().getItem(item);
	}
	
}
