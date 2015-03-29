package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;

public class BlockEmpty extends Block {

	public static final float DRAG = 0.7f;

	@Override
	public float getDragCoefficient(World world, int x, int y) {
		return DRAG;
	}

	@Override
	public void render(WorldRenderer world, int x, int y) {
		world.batch.setColor(Main.getRainbow(System.currentTimeMillis() - (x * 50) - (y * 50), 1f,
				0.25f));
		world.main.fillRect(x * World.tilesizex - world.camera.camerax,
				Main.convertY(y * World.tilesizey - world.camera.cameray), World.tilesizex,
				World.tilesizey);
	}
}
