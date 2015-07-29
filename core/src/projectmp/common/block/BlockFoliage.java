package projectmp.common.block;

import projectmp.client.WorldRenderer;

import com.badlogic.gdx.graphics.Color;

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
	public void renderInWorld(WorldRenderer renderer, float x, float y, float width, float height, int blockX, int blockY) {
		Color c = renderer.batch.getColor();
		if(shouldApplyFoliageColour) renderer.batch.setColor(0f, 175f / 255f, 17f / 255f, 1);
		super.renderInWorld(renderer, x, y, width, height, blockX, blockY);
		renderer.batch.setColor(c);
	}

}
