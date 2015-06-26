package projectmp.common.item;

import java.util.HashMap;

import projectmp.common.TexturedObject;
import projectmp.common.block.Block.BlockFaces;

import com.badlogic.gdx.utils.Array;

public class Items {

	private static Items instance;

	private Items() {
	}

	public static Items instance() {
		if (instance == null) {
			instance = new Items();
			instance.loadResources();
		}
		return instance;
	}

	private HashMap<String, Item> items = new HashMap<String, Item>();
	private HashMap<Item, String> reverse = new HashMap<Item, String>();
	private Array<Item> allItems = new Array<Item>();

	private void loadResources() {
		put("testItem", new ItemTest("testItem").addAnimation(Item.singleTexture("images/items/mininglaser.png")));
		put("chessPieceWhite", new ItemChessPiece("chessWhite"));
		put("chessPieceBlack", new ItemChessPiece("chessBlack"));
	}

	public void put(String key, Item value) {
		items.put(key, value);
		reverse.put(value, key);
		allItems.add(value);
	}

	public Item getItem(String key) {
		if (key == null) return null;
		return items.get(key);
	}

	public String getKey(Item item) {
		if (item == null) return null;
		return reverse.get(item);
	}

	public Array<Item> getItemList() {
		return allItems;
	}

}
