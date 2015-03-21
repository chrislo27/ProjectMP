package projectmp.achievements;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;

public class Achievements {

	private static Achievements instance;

	private Achievements() {
	}

	public static Achievements instance() {
		if (instance == null) {
			instance = new Achievements();
			instance.loadResources();
		}
		return instance;
	}

	public Array<Achievement> achievements;
	public HashMap<String, Integer> achievementId;

	private void add(Achievement a) {
		achievementId.put(a.data, achievements.size);
		achievements.add(a);
	}

	private void loadResources() {
		achievements = new Array<Achievement>();
		achievementId = new HashMap<String, Integer>();

		add(new Achievement("secret").setHidden(true).setSpecial(true));
		add(new Achievement("kraken").setHidden(true).setSpecial(true));
		add(new Achievement("test").setHidden(true));
		add(new Achievement("secretexit"));
	}
}
