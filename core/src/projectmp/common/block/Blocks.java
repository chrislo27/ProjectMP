package projectmp.common.block;

import java.util.HashMap;

import projectmp.common.block.Block.BlockFaces;

import com.badlogic.gdx.utils.Array;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;

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
	private Array<Block> allBlocks = new Array<Block>();

	private void loadResources() {
		put(defaultBlock, new BlockEmpty());
		put("stone",
				new BlockStone().solidify(BlockFaces.ALL).setOpaqueToLight()
						.addAnimation(Block.singleTexture("images/blocks/stone.png")));
		put("dirt",
				new BlockDirt().solidify(BlockFaces.ALL).setOpaqueToLight()
						.addAnimation(Block.singleTexture("images/blocks/dirt.png")));
		put("grass",
				new BlockGrass().solidify(BlockFaces.ALL).setOpaqueToLight()
						.addAnimation(Block.singleTexture("images/blocks/grass.png"))
						.addAnimation(Block.singleTexture("images/blocks/dirt.png")));
		put("tall_grass", new BlockTallGrass().addAnimation(Block
				.singleTexture("images/blocks/tall_grass.png")));
		put("chess_set", new BlockChessSet().addAnimation(Block.singleTexture("images/items/asteroidfirer.png")));
	}

	public void put(String key, Block value) {
		blocks.put(key, value);
		reverse.put(value, key);
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

	public Array<Block> getBlockList() {
		return allBlocks;
	}

	public static Block defaultBlock() {
		return instance().getBlock(defaultBlock);
	}

	public static final String defaultBlock = "empty";

}
