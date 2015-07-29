package projectmp.common.block;

import projectmp.client.WorldRenderer;

import com.badlogic.gdx.graphics.Color;

public class BlockFoliage extends Block {

	public BlockFoliage(String identifier) {
		super(identifier);
	}
	
	private float lastBatchColor = Color.WHITE.toFloatBits();
	
	public void startFoliaging(WorldRenderer renderer, int blockX, int blockY){
		lastBatchColor = renderer.batch.getPackedColor();
	}
	
	public void stopFoliaging(WorldRenderer renderer){
		renderer.batch.setColor(lastBatchColor);
	}

}
