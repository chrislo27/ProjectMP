package projectmp.common.item;

import projectmp.common.Translator;
import projectmp.common.inventory.itemstack.ItemStack;

import com.badlogic.gdx.utils.Array;


public class ItemScrapMetal extends Item{

	public ItemScrapMetal(String unlocalizedname) {
		super(unlocalizedname);
	}

	@Override
	public void addDescription(Array<String> array, ItemStack stack){
		array.add(Translator.getMsg("item." + getUnlocalizedName() + ".desc"));
	}
}
