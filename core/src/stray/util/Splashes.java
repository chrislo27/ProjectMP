package stray.util;

import stray.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Splashes {

	public static String getRandomSplash() {
		if (instance().splashes.size == 0) {
			return "WARNING MISSING SPLASHES";
		}
		return instance().splashes.random();
	}

	private static Splashes instance;

	private Splashes() {
	}

	public static Splashes instance() {
		if (instance == null) {
			instance = new Splashes();
			instance.loadResources();
		}
		return instance;
	}

	Array<String> splashes;

	private void loadResources() {
		String all = Gdx.files.internal("localization/splashes.txt").readString("UTF-8");
		splashes = new Array<String>(all.split("\\r?\\n|\\r"));
		
		splashes.shuffle();
	}

}
