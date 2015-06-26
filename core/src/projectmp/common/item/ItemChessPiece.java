package projectmp.common.item;

import projectmp.common.Translator;
import projectmp.common.inventory.itemstack.ItemStack;

import com.badlogic.gdx.math.MathUtils;


public class ItemChessPiece extends Item{

	public static final int PAWN = 1;
	public static final int KNIGHT = 2;
	public static final int BISHOP = 3;
	public static final int ROOK = 4;
	public static final int QUEEN = 5;
	public static final int KING = 6;
	
	public ItemChessPiece(String unlocalizedname) {
		super(unlocalizedname);
		this.setMaxStack(6);
	}
	
	@Override
	protected int getCurrentRenderingIndex(ItemStack stack){
		return MathUtils.clamp(stack.getAmount() - 1, 0, 5);
	}
	
	@Override
	public String getLocalizedName(ItemStack stack) {
		return Translator.instance().getMsg("item." + name + ".name", getCurrentRenderingIndex(stack));
	}

}
