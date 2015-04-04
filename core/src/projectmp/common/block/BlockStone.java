package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;

public class BlockStone extends Block {

	@Override
	public void render(WorldRenderer renderer, int x, int y) {
		renderer.batch.setColor(0.5f, 0.5f, 0.5f, 1);
		renderer.main.fillRect(x * World.tilesizex - renderer.camera.camerax,
				Main.convertY(y * World.tilesizey - renderer.camera.cameray), World.tilesizex,
				World.tilesizey);
		renderer.batch.setColor(1, 1, 1, 1);
	}

}
