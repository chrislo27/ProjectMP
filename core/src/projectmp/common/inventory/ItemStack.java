package projectmp.common.inventory;

import projectmp.common.item.Item;
import projectmp.common.item.Items;

public class ItemStack {

	String item;
	int quantity = 1;

	/**
	 * No-arg constructor that makes an empty ItemStack
	 */
	public ItemStack(){
		this(null, 0);
	}
	
	public ItemStack(String i, int amount) {
		item = i;
		quantity = amount;
	}

	/**
	 * Method to get the item from the stored string
	 * @return
	 */
	public Item getItem() {
		return Items.instance().getItem(item);
	}
	
	/**
	 * returns the actual string stored
	 * @return
	 */
	public String getItemString(){
		return item;
	}
	
	public int getAmount(){
		return quantity;
	}
	
	public void setAmount(int i){
		quantity = i;
	}
	
	public void setItem(String i){
		item = i;
	}
	
	public void setItem(Item i){
		item = Items.instance().getKey(i);
	}
	
	/**
	 * makes a deep copy
	 * @return
	 */
	public ItemStack copy(){
		ItemStack i = new ItemStack(item, quantity);
		
		return i;
	}
	
	public boolean isNothing(){
		return item == null || quantity <= 0;
	}
	
	public boolean equalsIgnoreAmount(ItemStack other){
		if(other.getItem() == this.getItem()){
			return true;
		}
		
		return false;
	}

}
