package projectmp.common.item;

import projectmp.client.WorldRenderer;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.util.Utils;
import projectmp.common.world.World;

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

		b.renderInWorld(renderer, x, y, width, height, -1, -1);
	}

	@Override
	public void onUsing(World world, EntityPlayer user, int slot, int x, int y) {
		if(!world.isServer) return;
		
		ItemStack stack = user.getInventoryObject().getSlot(slot);
		
		if(world.getBlock(x, y) == Blocks.getAir() && stack.getAmount() > 0){
			world.setBlock(block, x, y);
			stack.setAmount(stack.getAmount() - 1);
			
			world.main.serverLogic.updateClientsOfTotalInventoryChange("playerInv", Utils.unpackLongUpper(user.uuid), Utils.unpackLongLower(user.uuid));
		}
	}
	
}
