package projectmp.common.block;

public class Block {

	int collision = BlockFaces.NONE;
	
	public Block(){
		
	}
	
	public Block solidify(int faces){
		collision = faces;
		return this;
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
