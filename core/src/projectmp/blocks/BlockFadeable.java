package projectmp.blocks;

import projectmp.world.World;

public class BlockFadeable extends Block {

	public BlockFadeable(String path) {
		super(path);
	}

	@Override
	public void render(World world, int x, int y) {
		world.batch.setColor(1, 1, 1, getAlpha(world, x, y));
		
		super.render(world, x, y);

		world.batch.setColor(1, 1, 1, 1);
	}
	
	protected float getAlpha(World world, int x, int y){
		if((world.getBlock(x, y).isSolid(world, x, y)) == BlockFaces.NONE) return 0.075f;
		if(Block.entityIntersects(world, x + World.tilepartx, y + World.tileparty,
					world.getPlayer(), 1f - (World.tilepartx * 2), 1f - (World.tileparty * 2))) return 0.5f;
		
		return 1;
	}

}
