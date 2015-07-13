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
		put("chessWhite",
				new ItemChessPiece("chessWhite").addAnimations(
						Item.newSingleFrame("images/items/chess/white/pawn.png"),
						Item.newSingleFrame("images/items/chess/white/knight.png"),
						Item.newSingleFrame("images/items/chess/white/bishop.png"),
						Item.newSingleFrame("images/items/chess/white/rook.png"),
						Item.newSingleFrame("images/items/chess/white/queen.png"),
						Item.newSingleFrame("images/items/chess/white/king.png")));
		put("chessBlack",
				new ItemChessPiece("chessBlack").addAnimations(
						Item.newSingleFrame("images/items/chess/black/pawn.png"),
						Item.newSingleFrame("images/items/chess/black/knight.png"),
						Item.newSingleFrame("images/items/chess/black/bishop.png"),
						Item.newSingleFrame("images/items/chess/black/rook.png"),
						Item.newSingleFrame("images/items/chess/black/queen.png"),
						Item.newSingleFrame("images/items/chess/black/king.png")));
		put("testMineable", new ItemMineable("testMineable").addAnimations(Item.newSingleFrame("images/items/asteroidfirer.png")));
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
