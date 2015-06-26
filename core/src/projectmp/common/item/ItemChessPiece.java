package projectmp.common.item;

import projectmp.common.inventory.itemstack.ItemStack;

import com.badlogic.gdx.math.MathUtils;


public class ItemChessPiece extends Item{

	public ItemChessPiece(String unlocalizedname) {
		super(unlocalizedname);
		this.setMaxStack(10);
	}
	
	@Override
	protected int getCurrentRenderingIndex(ItemStack stack){
		return MathUtils.clamp(stack.getAmount() - 1, 0, 4);
	}

}
