package projectmp.common.block;

import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.world.World;

public class BlockEmpty extends Block {

	public BlockEmpty(String identifier) {
		super(identifier);
		setHardness(-1);
	}

	public static final float DRAG = 0.7f;

	@Override
	public float getDragCoefficient(World world, int x, int y) {
		return DRAG;
	}

	/**
	 * overrode to -1 so it doesn't render
	 */
	@Override
	public int getRenderingLayer(World world, int x, int y){
		return -1;
	}
	
	/**
	 * Overrode to prevent item drops
	 */
	@Override
	public ItemStack getDroppedItem(){
		return null;
	}
}
