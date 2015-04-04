package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;


public class BlockDirt extends Block {

	@Override
	public void render(WorldRenderer renderer, int x, int y) {
		renderer.batch.setColor(140 / 255f, 96 / 255f, 45 / 255f, 1);
		renderer.main.fillRect(x * World.tilesizex - renderer.camera.camerax,
				Main.convertY(y * World.tilesizey - renderer.camera.cameray), World.tilesizex,
				World.tilesizey);
		renderer.batch.setColor(1, 1, 1, 1);
	}
	
}
