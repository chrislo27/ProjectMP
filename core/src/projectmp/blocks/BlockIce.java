package projectmp.blocks;

import projectmp.world.World;


public class BlockIce extends Block{

	public BlockIce(String path) {
		super(path);
	}

	public float getDragCoefficient(World world, int x, int y) {
		return 0.175f;
	}
	
}
