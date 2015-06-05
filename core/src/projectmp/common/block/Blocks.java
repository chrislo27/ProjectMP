package projectmp.common.block;

import java.util.HashMap;

import projectmp.common.block.Block.BlockFaces;

import com.badlogic.gdx.utils.Array;

public class Blocks {

	private static Blocks instance;

	private Blocks() {
	}

	public static Blocks instance() {
		if (instance == null) {
			instance = new Blocks();
			instance.loadResources();
		}
		return instance;
	}

	private HashMap<String, Block> blocks = new HashMap<String, Block>();
	private HashMap<Block, String> reverse = new HashMap<Block, String>();
	private HashMap<Integer, Block> blockIDs = new HashMap<Integer, Block>();
	private HashMap<Block, Integer> reverseBlockIDs = new HashMap<Block, Integer>();
	private Array<Block> allBlocks = new Array<Block>();

	private void loadResources() {
		put(defaultBlock, 0, new BlockEmpty());
		put("stone", 1, new BlockStone().solidify(BlockFaces.ALL).setOpaqueToLight());
		put("dirt", 2, new BlockDirt().solidify(BlockFaces.ALL).setOpaqueToLight());
		put("grass", 3, 
				(Block) new BlockGrass().solidify(BlockFaces.ALL).setOpaqueToLight()
						.addAnimation(Block.singleBlockTexture("images/blocks/grass.png")));
	}

	public void put(String key, int id, Block value) {
		blocks.put(key, value);
		reverse.put(value, key);
		blockIDs.put(id, value);
		reverseBlockIDs.put(value, id);
		allBlocks.add(value);
	}

	public Block getBlock(String key) {
		if (key == null) return defaultBlock();
		return blocks.get(key);
	}

	public String getKey(Block block) {
		if (block == null) return defaultBlock;
		return reverse.get(block);
	}
	
	public Block getBlockFromID(int id){
		if(id <= -1) return defaultBlock();
		return blockIDs.get(id);
	}
	
	public int getIDFromBlock(Block block){
		if(block == null) return 0;
		return reverseBlockIDs.get(block);
	}
	
	public int getIDFromName(String name){
		if(name == null) return 0;
		return getIDFromBlock(getBlock(name));
	}
	
	public String getNameFromID(int id){
		if(id <= -1) return defaultBlock;
		return getKey(getBlockFromID(id));
	}

	public Array<Block> getBlockList() {
		return allBlocks;
	}

	public static Block defaultBlock() {
		return instance().getBlock(defaultBlock);
	}

	public static final String defaultBlock = "empty";

}
