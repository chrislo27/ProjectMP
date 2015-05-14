package projectmp.common.world.chunk;

import projectmp.common.block.Block;
import projectmp.common.block.Blocks;


public class Chunk {

	public static final int CHUNK_SIZE = 16;
	
	protected Block[][] blocks = new Block[CHUNK_SIZE][CHUNK_SIZE];
	protected byte[][] metadata = new byte[CHUNK_SIZE][CHUNK_SIZE];
	
	int locationX = 0;
	int locationY = 0;
	
	public Chunk(int locx, int locy){
		locationX = locx;
		locationY = locy;
		
		for(int x = 0; x < CHUNK_SIZE; x++){
			for(int y = 0; y < CHUNK_SIZE; y++){
				blocks[x][y] = Blocks.defaultBlock();
				metadata[x][y] = 0;
			}
		}
	}
	
	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= CHUNK_SIZE || y >= CHUNK_SIZE) return Blocks.defaultBlock();
		if (blocks[x][y] == null) return Blocks.defaultBlock();
		return blocks[x][y];
	}

	public int getMeta(int x, int y) {
		if (x < 0 || y < 0 || x >= CHUNK_SIZE || y >= CHUNK_SIZE) return 0;
		return metadata[x][y];
	}

	public void setBlock(Block b, int x, int y) {
		if (x < 0 || y < 0 || x >= CHUNK_SIZE || y >= CHUNK_SIZE) return;
		blocks[x][y] = b;
	}

	public void setMeta(int m, int x, int y) {
		if (x < 0 || y < 0 || x >= CHUNK_SIZE || y >= CHUNK_SIZE) return;
		metadata[x][y] = (byte) m;
	}
	
}
