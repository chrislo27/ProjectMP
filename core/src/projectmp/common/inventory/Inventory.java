package projectmp.common.inventory;

import projectmp.common.util.MathHelper;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * contains ItemStacks, do not confuse with IInventory
 * 
 *
 */
public class Inventory {

	public static final int PLAYER_SLOTS = 10;

	ItemStack[] items = new ItemStack[1];
	int maxCapacity = 1;

	public Inventory() {

	}

	/**
	 * sets max capacity AND re-inits itemstack array
	 * @param cap
	 * @return
	 */
	public Inventory setMaxCapacity(int cap) {
		maxCapacity = cap;
		items = new ItemStack[cap];

		return this;
	}
	
	public ItemStack getSlot(int slot){
		return items[slot];
	}
	
	public void setSlot(int slot, ItemStack stack){
		items[slot] = stack;
	}
	
	/**
	 * makes the slots with the capacity
	 */
	public void initSlots(){
		items = new ItemStack[maxCapacity];
		for(int i = 0; i < maxCapacity; i++){
			items[i] = new ItemStack();
		}
	}

}
