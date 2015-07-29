package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * renders one index up then the normal index
 * 
 *
 */
public class BlockGrass extends BlockFoliage {

	public BlockGrass(String identifier) {
		super(identifier);
	}

	@Override
	public void renderInWorld(WorldRenderer renderer, float x, float y, float width, float height,
			int blockX, int blockY) {
		int renderingIndex = getCurrentRenderingIndex(renderer.world, blockX, blockY);
		
		shouldApplyFoliageColour = false;
		renderIndexAt(renderer.batch, renderer.main, renderer.world, x, y, width, height, renderingIndex + 1, blockX, blockY);
		shouldApplyFoliageColour = true;
		renderIndexAt(renderer.batch, renderer.main, renderer.world, x, y, width, height, renderingIndex, blockX, blockY);
	}

}
