package projectmp.common.inventory;

import java.util.List;

import projectmp.common.Main;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.io.CanBeSavedToNBT;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;
import com.evilco.mc.nbt.tag.TagList;
import com.evilco.mc.nbt.tag.TagString;

/**
 * contains ItemStacks, do not confuse with IInventory
 * 
 *
 */
public class Inventory implements CanBeSavedToNBT{

	ItemStack[] items = new ItemStack[1];
	int maxCapacity = 1;
	
	public String invId = null;
	public int invX = 0;
	public int invY = 0;

	public Inventory(int maxCap, String id, int x, int y){
		setMaxCapacity(maxCap);
		invId = id;
		invX = x;
		invY = y;
	}
	
	public Inventory() {
		
	}
	
	/**
	 * Adds as much of the given item stack into this inventory. The ItemStack passed in will be directly modified
	 * @param stack
	 */
	public void addStack(ItemStack stack){
		for(int i = 0; i < maxCapacity; i++){
			ItemStack is = getSlot(i);
			
			
		}
	}

	/**
	 * sets max capacity AND re-inits itemstack array
	 * @param cap
	 * @return
	 */
	public Inventory setMaxCapacity(int cap) {
		maxCapacity = cap;
		initSlots();

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

	@Override
	public void writeToNBT(TagCompound tag) {
		TagCompound essential = new TagCompound("Metadata");
		essential.setTag(new TagString("InvID", invId));
		essential.setTag(new TagInteger("InvX", invX));
		essential.setTag(new TagInteger("InvY", invY));
		essential.setTag(new TagInteger("MaxCapacity", maxCapacity));
		tag.setTag(essential);
		
		TagList itemsList = new TagList("Items");
		for(int i = 0; i < maxCapacity; i++){
			TagCompound itemstack = new TagCompound("Item" + i);
			itemstack.setTag(new TagInteger("SlotNum", i));
			getSlot(i).writeToNBT(itemstack);
			itemsList.addTag(itemstack);
		}
		tag.setTag(itemsList);
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		TagCompound essential = tag.getCompound("Metadata");
		this.invId = essential.getString("InvID");
		this.invX = essential.getInteger("InvX");
		this.invY = essential.getInteger("InvY");
		this.maxCapacity = essential.getInteger("MaxCapacity");
		
		initSlots();
		
		List<TagCompound> itemsList = tag.getList("Items", TagCompound.class);
		for(int i = 0; i < itemsList.size(); i++){
			ItemStack is = new ItemStack(null, 0);
			is.readFromNBT(itemsList.get(i));
					
			setSlot(itemsList.get(i).getInteger("SlotNum"), is);
		}
	}

}
