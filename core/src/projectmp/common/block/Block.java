package projectmp.common.block;

import projectmp.common.entity.Entity;
import projectmp.common.world.World;

public class Block {

	int collision = BlockFaces.NONE;
	
	public Block(){
		
	}
	
	public Block solidify(int faces){
		collision = faces;
		return this;
	}
	
	public int isSolid(World world, int x, int y){
		return collision;
	}
	
	public float getDragCoefficient(World world, int x, int y){
		return 1;
	}
	
	public void onCollideLeftFace(World world, int x, int y, Entity e) {

	}

	public void onCollideRightFace(World world, int x, int y, Entity e) {

	}

	public void onCollideUpFace(World world, int x, int y, Entity e) {

	}

	public void onCollideDownFace(World world, int x, int y, Entity e) {

	}
	
	public static class BlockFaces {

		public static final int NONE = 0x0;
		public static final int ALL = 0xF;
		public static final int UP = 0x1;
		public static final int DOWN = 0x2;
		public static final int LEFT = 0x4;
		public static final int RIGHT = 0x8;
		
	}

}
