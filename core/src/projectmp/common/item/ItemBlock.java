package projectmp.common.item;

import projectmp.client.WorldRenderer;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.inventory.itemstack.ItemStack;


public class ItemBlock extends Item{

	private String block;
	
	public ItemBlock(String block) {
		super("block_" + block);
		this.block = block;
	}

	public ItemBlock setBlock(String b){
		block = b;
		return this;
	}
	
	public String getLocalizedName(ItemStack stack) {
		return Blocks.instance().getBlock(block).getLocalizedName(stack);
	}

	public void render(WorldRenderer renderer, float x, float y, float width, float height, ItemStack stack) {
		Block b = Blocks.instance().getBlock(block);
		float spacing = 0.25f;
		
		b.renderIndexAt(renderer.batch, renderer.main, renderer.world, x + (spacing * width / 2),
				y + (spacing * height / 2), width - (width * spacing),
				height - (height * spacing), this.getCurrentRenderingIndex(stack), -1, -1);
	}
	
}
