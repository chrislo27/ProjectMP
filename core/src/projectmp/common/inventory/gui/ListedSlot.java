package projectmp.common.inventory.gui;

import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.item.Item;

import com.badlogic.gdx.utils.Array;

public class ListedSlot extends Slot {

	boolean whitelist = true;
	Array<String> listItems = new Array<>();
	Array<Class<? extends Item>> listClasses = new Array<>();

	public ListedSlot(Inventory inventory, int slotNumber, int x, int y, boolean whitelist) {
		super(inventory, slotNumber, x, y);
		this.whitelist = whitelist;
	}

	public ListedSlot addItem(String item){
		listItems.add(item);
		return this;
	}
	
	public ListedSlot addItem(Class<? extends Item> item){
		listClasses.add(item);
		return this;
	}
	
	@Override
	public boolean canPlaceItem(ItemStack stack) {
		for (String s : listItems) {
			if (s.equals(stack.getItemString())) {
				return whitelist;
			}
		}
		
		for(Class<? extends Item> clazz : listClasses){
			if(clazz.isInstance(stack.getItem())){
				return whitelist;
			}
		}

		return false;
	}

}
