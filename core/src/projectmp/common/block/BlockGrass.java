package projectmp.common.block;

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
	public void renderIndexAt(Batch batch, Main main, World world, float x, float y, float width, float height, int renderingIndex, int blockX, int blockY) {
		shouldApplyFoliageColour = false;
		super.renderIndexAt(batch, main, world, x, y, width, height, renderingIndex + 1, blockX, blockY);
		shouldApplyFoliageColour = true;
		super.renderIndexAt(batch, main, world, x, y, width, height, renderingIndex, blockX, blockY);
	}

}
