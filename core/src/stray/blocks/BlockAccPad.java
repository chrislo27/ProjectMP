package stray.blocks;

import stray.world.World;


public class BlockAccPad extends Block{

	public BlockAccPad(String path) {
		super(path);
	}
	
	public float getDragCoefficient(World world, int x, int y) {
		return -1.5f;
	}

}
