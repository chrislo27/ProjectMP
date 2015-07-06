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

	@Override
	public void render(WorldRenderer renderer, int x, int y) {
		Color c = renderer.batch.getColor();
		renderer.batch.setColor(0f, 175f / 255f, 17f / 255f, 1);
		super.render(renderer, x, y);
		renderer.batch.setColor(c);
	}

}
