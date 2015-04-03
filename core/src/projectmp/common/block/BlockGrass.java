package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;


public class BlockGrass extends BlockDirt {

	@Override
	public void render(WorldRenderer world, int x, int y) {
		world.batch.setColor(40 / 255f, 176 / 255f, 50 / 255f, 1);
		world.main.fillRect(x * World.tilesizex - world.camera.camerax,
				Main.convertY(y * World.tilesizey - world.camera.cameray), World.tilesizex,
				World.tilesizey);
		world.batch.setColor(1, 1, 1, 1);
	}
	
}
