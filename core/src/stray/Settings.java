package stray;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	private static Settings instance;

	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;

	public static boolean showFPS = true;
	public static boolean debug = false;
	public static boolean showVignette = true;
	public static float musicVolume = 1;
	public static float soundVolume = 1;
	
	static{
		Settings.instance(); // init settings
	}

	private Settings() {
	}

	public static Settings instance() {
		if (instance == null) {
			instance = new Settings();
			instance.loadResources();
		}
		return instance;
	}

	private Preferences pref;

	private void loadResources() {
		pref = Main.getPref("settings");

		showFPS = pref.getBoolean("showFPS", true);
		showVignette = pref.getBoolean("vignette", true);
		soundVolume = pref.getFloat("soundVolume", 1f);
		musicVolume = pref.getFloat("musicVolume", 1f);
	}

	public void save() {
		pref.putBoolean("showFPS", Settings.showFPS).putBoolean("vignette", showVignette)
				.putFloat("sound", soundVolume).putFloat("music", musicVolume).flush();
	}

	public static Preferences getPreferences() {
		return instance().pref;
	}

}
