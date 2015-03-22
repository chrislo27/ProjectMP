package common.util;

import java.util.HashMap;

public class Difficulty {

	int id = -1;
	public float damageMultiplier = 1;

	public Difficulty(int id) {
		this.id = id;
	}
	
	public Difficulty setDmgMultiplier(float d){
		damageMultiplier = d;
		return this;
	}

	public static final int EASY_ID = 0;
	public static final int NORMAL_ID = 1;
	public static final int HARD_ID = 2;
	
	private static HashMap<Integer, Difficulty> difficulties = null;

	public static HashMap<Integer, Difficulty> get() {
		if (difficulties == null) {
			difficulties = new HashMap<Integer, Difficulty>();
			loadResources();
		}
		return difficulties;
	}

	private static void loadResources() {
		difficulties.put(EASY_ID, new Difficulty(EASY_ID).setDmgMultiplier(0.5f));
		difficulties.put(NORMAL_ID, new Difficulty(NORMAL_ID).setDmgMultiplier(1f));
		difficulties.put(HARD_ID, new Difficulty(HARD_ID).setDmgMultiplier(2f));
	}
}
