package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;


public class BlockGrass extends BlockDirt {

	@Override
	public void render(WorldRenderer renderer, int x, int y) {
		renderer.batch.setColor(40 / 255f, 176 / 255f, 50 / 255f, 1);
		renderer.main.fillRect(renderer.convertWorldX(x), renderer.convertWorldY(y, World.tilesizey), World.tilesizex,
				World.tilesizey);
		renderer.batch.setColor(1, 1, 1, 1);
	}
	
}
