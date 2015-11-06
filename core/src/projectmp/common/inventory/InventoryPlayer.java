package projectmp.common.inventory;

import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.item.Items;
import projectmp.common.util.Utils;

import com.badlogic.gdx.math.MathUtils;


public class InventoryPlayer extends Inventory{
	
	public static final int MAX_INV_CAPACITY = 36;
	
	public InventoryPlayer(){
		super();
	}
	
	public InventoryPlayer(long uuid){
		super(MAX_INV_CAPACITY, "playerInv", Utils.unpackLongUpper(uuid), Utils.unpackLongLower(uuid));
		
		setSlot(0, new ItemStack("plasmaCutter", 1));
		setSlot(1, new ItemStack("scrapMetal", 1));
		setSlot(2, new ItemStack("block_testCable", 999));
		setSlot(3, new ItemStack("block_testBattery", 999));
		setSlot(4, new ItemStack("block_testGenerator", 999));
	}
	
	@Override
	public InventoryPlayer setMaxCapacity(int i){
		super.setMaxCapacity(i);
		return this;
	}
	
}
