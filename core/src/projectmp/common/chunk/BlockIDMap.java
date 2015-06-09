package projectmp.common.chunk;

import java.util.HashMap;
import java.util.Map;

import projectmp.common.block.Block;
import projectmp.common.block.Blocks;

import com.badlogic.gdx.utils.Array;
import com.evilco.mc.nbt.tag.ITag;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;

public class BlockIDMap {

	public static final String COMPOUND_TAG_NAME = "BlockIDMap";

	private static BlockIDMap instance;

	private BlockIDMap() {
	}

	public static BlockIDMap instance() {
		if (instance == null) {
			instance = new BlockIDMap();
			instance.loadResources();
		}
		return instance;
	}

	public HashMap<String, Integer> stringToID = new HashMap<>();
	public HashMap<Integer, String> idToString = new HashMap<>();

	private void loadResources() {

	}

	public void setMap(TagCompound comp) {
		stringToID.clear();
		idToString.clear();

		Map<String, ITag> tags = comp.getTags();
		for (ITag tag : tags.values()) {
			TagInteger intTag = (TagInteger) tag;

			stringToID.put(intTag.getName(), intTag.getValue());
			idToString.put(intTag.getValue(), intTag.getName());
		}
	}

	public static TagCompound generateBlockIDMap() {
		TagCompound comp = new TagCompound(COMPOUND_TAG_NAME);
		Array<Block> allBlocks = Blocks.instance().getBlockList();

		for (int i = 0; i < allBlocks.size; i++) {
			Block block = allBlocks.get(i);

			TagInteger t = new TagInteger(Blocks.instance().getKey(block), i);
			comp.setTag(t);
		}

		return comp;
	}

}
