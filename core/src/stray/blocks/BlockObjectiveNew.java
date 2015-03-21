package stray.blocks;

import stray.objective.Objectives;
import stray.world.World;

public class BlockObjectiveNew extends Block {

	public BlockObjectiveNew(String path) {
		super(path);
	}

	@Override
	public boolean isRenderedFront() {
		return true;
	}
	
	@Override
	public void tickUpdate(World world, int x, int y) {
		super.tickUpdate(world, x, y);
		
		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			world.addObjective(Objectives.instance().map.get(world.getMeta(x, y)));
			world.setBlock(null, x, y);
			
			for(int i = 0; i < 32; i++){
				BlockCollectible.glowyParticles(world, x, y);
			}
		}
	}
	
	@Override
	public void render(World world, int x, int y) {
		super.renderWithOffset(world, x, y, 0, BlockCollectible.getFloatingOffset(world, x, y));
	}

}
