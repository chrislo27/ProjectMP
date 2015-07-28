package projectmp.common.block;

import java.util.HashMap;

import projectmp.common.block.Block.BlockFaces;
import projectmp.common.item.ItemBlock;
import projectmp.common.item.Items;

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
	private Array<Block> allBlocks = new Array<Block>();

	private void loadResources() {
		put(airBlock, new BlockEmpty("empty"));
		put("stone", new BlockStone("stone").solidify(BlockFaces.ALL).setOpaqueToLight()
				.addAnimations(Block.newSingleFrame("images/blocks/stone.png")));
		put("dirt",
				new BlockDirt("dirt").solidify(BlockFaces.ALL).setOpaqueToLight()
						.addAnimations(Block.newSingleFrame("images/blocks/dirt.png")));
		put("grass",
				new BlockGrass("grassBlock").solidify(BlockFaces.ALL).setOpaqueToLight()
						.addAnimations(Block.newSingleFrame("images/blocks/grass.png"), Block.newSingleFrame("images/blocks/dirt.png")));
		put("tall_grass", new BlockTallGrass("tallGrass").addAnimations(Block
				.newSingleFrame("images/blocks/tall_grass.png")));
		put("chess_set", new BlockChessSet("chessboard"));
		
		// experimental stuff here
		put("testCable", new BlockCable("testCable", 16).addAnimations(Block.newSingleFrame("images/blocks/test/cable.png")));
		put("testBattery", new BlockTestBattery("testBattery").addAnimations(Block.newSingleFrame("images/blocks/test/battery.png")));
		put("testGenerator", new BlockTestGenerator("testGenerator").addAnimations(Block.newSingleFrame("images/blocks/test/generator.png")));
	}

	public void put(String key, Block value) {
		// add to lists and maps
		blocks.put(key, value);
		reverse.put(value, key);
		allBlocks.add(value);
		
		// create the item instance
		ItemBlock itemBlock = new ItemBlock(key);
		Items.instance().put("block_" + key, itemBlock);
		
		// initialize the block
		value.initialize();
	}

	public Block getBlock(String key) {
		if (key == null) return getAir();
		return blocks.get(key);
	}

	public String getKey(Block block) {
		if (block == null) return airBlock;
		return reverse.get(block);
	}

	public Array<Block> getBlockList() {
		return allBlocks;
	}

	public static Block getAir() {
		return instance().getBlock(airBlock);
	}

	public static final String airBlock = "empty";

}
