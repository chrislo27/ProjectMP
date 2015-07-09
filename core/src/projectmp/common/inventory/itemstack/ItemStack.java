package projectmp.common.inventory.itemstack;

import projectmp.common.io.CanBeSavedToNBT;
import projectmp.common.item.Item;
import projectmp.common.item.Items;
import projectmp.common.util.NBTUtils;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;
import com.evilco.mc.nbt.tag.TagString;

public class ItemStack implements CanBeSavedToNBT{

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

	@Override
	public void writeToNBT(TagCompound tag) {
		tag.setTag(new TagString("Item", (item == null ? "" : item)));
		tag.setTag(new TagInteger("Num", quantity));
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		item = NBTUtils.getStringWithDef(tag, "Item", null);
		quantity = NBTUtils.getIntWithDef(tag, "Num", 0);
		
		if(item == "") item = null;
	}

}
