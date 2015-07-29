package projectmp.common.inventory;

import projectmp.common.inventory.itemstack.ItemStack;

import com.badlogic.gdx.utils.Array;


public interface Describable {

	public void addDescription(Array<String> array, ItemStack stack);
	
}
