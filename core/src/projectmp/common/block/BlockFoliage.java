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
		
		renderer.batch.setColor(0f, 175f / 255f, 17f / 255f, 1);
	}
	
	public void stopFoliaging(WorldRenderer renderer){
		renderer.batch.setColor(lastBatchColor);
	}

}
