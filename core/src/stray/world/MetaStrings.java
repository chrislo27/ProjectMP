package stray.world;

import java.util.HashMap;

/**
 * correlates a block metadata integer with a string
 * 
 *
 */
public class MetaStrings {
	
	private static MetaStrings instance;

	private MetaStrings() {
	}

	public static MetaStrings instance() {
		if (instance == null) {
			instance = new MetaStrings();
			instance.loadResources();
		}
		return instance;
	}

	public HashMap<Integer, String> map = new HashMap<Integer, String>();
	public HashMap<String, Integer> reverse = new HashMap<String, Integer>();
	
	private void loadResources() {
		put(0, "missing_meta");
		
	}
	
	private void put(int index, String s){
		map.put(index, s);
		reverse.put(s, index);
	}
	
}
