package projectmp.common.util;

import java.util.HashMap;

public class AssetMap {

	private static AssetMap instance;

	private AssetMap() {
	}

	public static AssetMap instance() {
		if (instance == null) {
			instance = new AssetMap();
			instance.loadResources();
		}
		return instance;
	}

	HashMap<String, String> map;

	private void loadResources() {
		map = new HashMap<String, String>();

	}

	/**
	 * get name of a path (uses instance())
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return AssetMap.instance().map.get(key);
	}

	/**
	 * add a key/value pair
	 * 
	 * @param key
	 * @param value
	 * @return value
	 */
	public static String add(String key, String value) {
		AssetMap.instance().map.put(key, value);
		return value;
	}

	/**
	 * add a key/value pair - parameters swapped!!!
	 * 
	 * @param value
	 * @param key
	 * @return value
	 */
	public static String addSwap(String value, String key) {
		AssetMap.instance().map.put(key, value);
		return value;
	}

	public static boolean containsKey(String key) {
		return AssetMap.instance().map.containsKey(key);
	}

	public static boolean containsValue(String value) {
		return AssetMap.instance().map.containsValue(value);
	}
}
