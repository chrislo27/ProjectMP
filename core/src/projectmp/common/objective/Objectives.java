package projectmp.common.objective;

import java.util.HashMap;

public class Objectives {

	private static Objectives instance;

	private Objectives() {
	}

	public static Objectives instance() {
		if (instance == null) {
			instance = new Objectives();
			instance.loadResources();
		}
		return instance;
	}

	public HashMap<Integer, String> map = new HashMap<Integer, String>();
	public HashMap<String, Integer> reverse = new HashMap<String, Integer>();

	private void loadResources() {
		put(0, "complete_level");
		put(1, "movehor");
		put(2, "reversefall");

	}

	private void put(int index, String s) {
		map.put(index, s);
		reverse.put(s, index);
	}

}
