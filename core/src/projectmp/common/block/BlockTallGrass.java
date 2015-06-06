package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.util.MathHelper;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class BlockTallGrass extends Block{

	private float[] vertices = new float[20];
	
	@Override
	public void render(WorldRenderer renderer, int x, int y){
		TextureRegion region = getAnimation(getCurrentRenderingIndex(renderer.world, 0, 0))
				.getCurrentFrame();
		int idx = 0;
		float offsetx = (MathHelper.clampNumberFromTime(System.currentTimeMillis(), 3f)) * 32;
		
		// bottom left
		vertices[idx++] = renderer.convertWorldX(x);
		vertices[idx++] = renderer.convertWorldY(y, World.tilesizey);
		vertices[idx++] = renderer.batch.getColor().toFloatBits();
		vertices[idx++] = region.getU();
		vertices[idx++] = region.getV2();
		
		// top left
		vertices[idx++] = renderer.convertWorldX(x) + offsetx;
		vertices[idx++] = renderer.convertWorldY(y, World.tilesizey) + region.getRegionHeight();
		vertices[idx++] = renderer.batch.getColor().toFloatBits();
		vertices[idx++] = region.getU();
		vertices[idx++] = region.getV();
		
		// top right
		vertices[idx++] = renderer.convertWorldX(x) + offsetx + region.getRegionWidth();
		vertices[idx++] = renderer.convertWorldY(y, World.tilesizey) + region.getRegionHeight();
		vertices[idx++] = renderer.batch.getColor().toFloatBits();
		vertices[idx++] = region.getU2();
		vertices[idx++] = region.getV();
		
		// bottom right
		vertices[idx++] = renderer.convertWorldX(x) + region.getRegionWidth();
		vertices[idx++] = renderer.convertWorldY(y, World.tilesizey);
		vertices[idx++] = renderer.batch.getColor().toFloatBits();
		vertices[idx++] = region.getU2();
		vertices[idx++] = region.getV2();
		
		renderer.batch.draw(region.getTexture(), vertices, 0, vertices.length);
		
//		super.render(renderer, x, y);
	}
	
}
