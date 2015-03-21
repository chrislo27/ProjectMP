package stray.achievements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

public class CompletedAchievements {

	public CompletedAchievements() {
		loadResources();
	}

	HashMap<Achievement, Boolean> completed = new HashMap<Achievement, Boolean>();

	public HashMap<Achievement, Boolean> getAll() {
		return completed;
	}

	public void complete(Achievement a) {
		completed.put(a, true);
	}

	private void loadResources() {
		Array<Achievement> ach = Achievements.instance().achievements;
		for (Achievement a : ach) {
			completed.put(a, false);
		}
	}

	public void save(String prefix, Preferences p) {
		Iterator it = completed.entrySet().iterator();

		while (it.hasNext()) {
			Entry<Achievement, Boolean> entry = (Entry) it.next();
			if (((Boolean) entry.getValue()) == true) {
				p.putBoolean(prefix + entry.getKey().data, true);
			}
		}
		p.flush();
	}

	public void load(String prefix, Preferences p) {
		Iterator it = p.get().entrySet().iterator();
		HashMap<Achievement, Boolean> toPut = new HashMap();
		while (it.hasNext()) {
			Entry<String, ?> entry = (Entry) it.next();
			if (entry.getKey().startsWith(prefix)) {
				for (Achievement a : completed.keySet()) {
					if (a.data.equals(entry.getKey().substring(prefix.length()))) {
						toPut.put(a, true);
					}
				}
			}
		}
		if (!toPut.isEmpty()) {
			completed.putAll(toPut);
		}
	}

}
