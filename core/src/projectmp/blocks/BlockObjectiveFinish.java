package projectmp.blocks;

import projectmp.Main;
import projectmp.objective.Objective;
import projectmp.objective.Objectives;
import projectmp.world.World;


public class BlockObjectiveFinish extends Block{

	public BlockObjectiveFinish(String path) {
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
			world.completeObjective(Objectives.instance().map.get(world.getMeta(x, y)));
			world.setBlock(null, x, y);
			
			for(int i = 0; i < 32; i++){
				BlockCollectible.glowyParticles(world, x, y);
			}
		}
	}
	
	@Override
	public void render(World world, int x, int y) {
		if(world.main.getScreen() != Main.LEVELEDITOR) world.batch.setColor(1, 1, 1, 0.1f);
		
		for(Objective o : world.objectives){
			if(world.getMeta(x, y) == Objectives.instance().reverse.get(o.id)){
				world.batch.setColor(1, 1, 1, 1);
				break;
			}
		}
		
		super.renderWithOffset(world, x, y, 0, BlockCollectible.getFloatingOffset(world, x, y));
		world.batch.setColor(1, 1, 1, 1);
	}
}
