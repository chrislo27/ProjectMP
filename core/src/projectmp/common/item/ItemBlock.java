package projectmp.common.item;

import projectmp.client.WorldRenderer;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.inventory.itemstack.ItemStack;

public class ItemBlock extends Item {

	private String block;

	public ItemBlock(String block) {
		super("block_" + block);
		this.block = block;
	}

	public ItemBlock setBlock(String b) {
		block = b;
		return this;
	}
	
	@Override
	public String getLocalizedName(ItemStack stack) {
		return Blocks.instance().getBlock(block).getLocalizedName(stack);
	}

	@Override
	public void render(WorldRenderer renderer, float x, float y, float width, float height,
			ItemStack stack) {
		Block b = Blocks.instance().getBlock(block);

		b.renderIndexAt(renderer.batch, renderer.main, renderer.world, x, y, width, height,
				this.getCurrentRenderingIndex(stack), -1, -1);
	}

	
}
