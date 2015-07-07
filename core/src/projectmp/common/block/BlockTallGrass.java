package projectmp.common.block;

import java.util.ArrayList;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.entity.Entity;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Sizeable;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class BlockTallGrass extends Block {

	public BlockTallGrass(String identifier) {
		super(identifier);
	}

	private float[] vertices = new float[20];
	private GrassBoundingBox bounds = new GrassBoundingBox();

	@Override
	public void render(WorldRenderer renderer, int x, int y) {
		float c = renderer.batch.getColor().toFloatBits();
		TextureRegion region = getAnimation(getCurrentRenderingIndex(renderer.world, 0, 0))
				.getCurrentFrame();
		int idx = 0;

		float worldQuarter = (renderer.world.sizey / 4f);
		
		// wind speed calculated based on y-level
		float windSpeed = MathUtils.clamp(10f * (y / worldQuarter), 0, 10f);
		if(y > worldQuarter) windSpeed = 0;
		
		// the actual "waviness" of it, there's an x-axis offset on the time
		float wave = (MathHelper.clampNumberFromTime(System.currentTimeMillis() - (x * 250), windSpeed) - 0.25f)
				* World.tilesizex;
		
		// when entities run into it
		float offsetx = wave + (renderer.world.getMeta(x, y));

		renderer.batch.setColor(0f, 175f / 255f, 17f / 255f, 1);

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
		renderer.batch.setColor(c);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		super.tickUpdate(world, x, y);
		
		if (world.isServer) return;

		if (world.getMeta(x, y) != 0) {
			world.setMeta((int) (world.getMeta(x, y) - (world.getMeta(x, y) * 0.01f)), x, y);
		}

		bounds.x = x;
		bounds.y = y + World.tileparty;
		bounds.width = 1;
		bounds.height = 1f - World.tileparty;
		ArrayList<Entity> nearby = world.getQuadArea(bounds);
		
		for (int i = 0; i < nearby.size(); i++) {
			Entity e = nearby.get(i);
			if (e.velox == 0) continue;
			
			if (MathHelper.intersects(bounds.x, bounds.y, bounds.width, bounds.height, e.visualX,
					e.visualY, e.sizex, e.sizey)) {

				int m = world.getMeta(x, y);
				world.setMeta((int) MathUtils.clamp(m + (e.velox * World.tilesizey * 0.05f),
						-World.tilesizex / 2, World.tilesizex / 2), x, y);
			}
		}
	}
	
	@Override
	public int getRenderingLayer(World world, int x, int y){
		return 1;
	}

	private static class GrassBoundingBox implements Sizeable {

		public float x, y, width, height;

		@Override
		public float getX() {
			return x;
		}

		@Override
		public float getY() {
			return y;
		}

		@Override
		public float getWidth() {
			return width;
		}

		@Override
		public float getHeight() {
			return height;
		}

	}

}
