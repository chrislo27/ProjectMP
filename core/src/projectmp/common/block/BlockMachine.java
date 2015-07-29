package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.tileentity.TileEntity;

/**
 * A block that extends BlockPowerHandler and has a custom render method
 * that renders the main sprite and then a bypassed sprite with an index
 * 1 more than the current rendering index. This is generally for the glowy
 * part (electrical bits) of a machine.
 * 
 *
 */
public abstract class BlockMachine extends BlockPowerHandler{

	boolean shouldRenderGlowing = true;
	
	public BlockMachine(String unlocalName) {
		super(unlocalName);
	}
	
	@Override
	public void renderInWorld(WorldRenderer renderer, float x, float y, float width, float height, int blockX, int blockY){
		// render main part
		renderIndexAt(renderer.batch, renderer.main, renderer.world, x, y, width, height,
				getCurrentRenderingIndex(renderer.world, blockX, blockY), blockX, blockY);
		
		if(!shouldRenderGlowing) return;
		
		// render glowing
		renderer.startBypassing();
		renderIndexAt(renderer.batch, renderer.main, renderer.world, x, y, width, height,
				getCurrentRenderingIndex(renderer.world, blockX, blockY) + 1, blockX, blockY);
		renderer.stopBypassing();
	}

}
