package projectmp.common.block;

import projectmp.common.Main;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * renders with a tint
 * 
 *
 */
public class BlockFoliage extends Block {

	public BlockFoliage(String identifier) {
		super(identifier);
	}

	protected boolean shouldApplyFoliageColour = true;
	
	@Override
	public void renderIndexAt(Batch batch, Main main, World world, float x, float y, float width, float height, int renderingIndex, int blockX, int blockY) {
		Color c = batch.getColor();
		if(shouldApplyFoliageColour) batch.setColor(0f, 175f / 255f, 17f / 255f, 1);
		super.renderIndexAt(batch, main, world, x, y, width, height, renderingIndex, blockX, blockY);
		batch.setColor(c);
	}

}
