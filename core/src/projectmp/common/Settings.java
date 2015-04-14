package projectmp.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	private static Settings instance;

	public static final int DEFAULT_WIDTH = 1290;
	public static final int DEFAULT_HEIGHT = 720;
	public static final int DEFAULT_PORT = 27299;

	public static boolean showFPS = true;
	public static boolean debug = false;
	public static boolean showVignette = true;
	public static float musicVolume = 1;
	public static float soundVolume = 1;

	public static boolean smoothLighting = true;

	public static int actualWidth = DEFAULT_WIDTH;
	public static int actualHeight = DEFAULT_HEIGHT;
	public static boolean fullscreen = false;

	static {
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
		smoothLighting = pref.getBoolean("smoothLighting", true);
		actualWidth = pref.getInteger("actualWidth", DEFAULT_WIDTH);
		actualHeight = pref.getInteger("actualHeight", DEFAULT_HEIGHT);
	}

	public void save() {
		pref.putBoolean("showFPS", Settings.showFPS).putBoolean("vignette", showVignette)
				.putFloat("sound", soundVolume).putFloat("music", musicVolume)
				.putBoolean("smoothLighting", true).putInteger("actualWidth", actualWidth)
				.putInteger("actualHeight", actualHeight).flush();
	}

	public static Preferences getPreferences() {
		return instance().pref;
	}

	private static final Resolution[] resolutions43 = new Resolution[] { new Resolution(640, 480),
			new Resolution(800, 600), new Resolution(1024, 768), new Resolution(1152, 864),
			new Resolution(1280, 960) };
	private static final Resolution[] resolutions169 = new Resolution[] {
			new Resolution(1176, 664), new Resolution(1280, 720), new Resolution(1360, 768),
			new Resolution(1366, 768), new Resolution(1600, 900), new Resolution(1768, 992),
			new Resolution(1920, 1080) };
	private static final Resolution[] resolutions1610 = new Resolution[] {
			new Resolution(1280, 800), new Resolution(1440, 900), new Resolution(1600, 1024),
			new Resolution(1680, 750) };

	public static Resolution[] get43ResolutionsList() {
		return resolutions43;
	}

	public static Resolution[] get169ResolutionsList() {
		return resolutions169;
	}

	public static Resolution[] get1610ResolutionsList() {
		return resolutions1610;
	}

	public static class Resolution {

		int width = 0;
		int height = 0;

		public Resolution(int w, int h) {
			width = w;
			height = h;
		}

		@Override
		public String toString() {
			return width + "x" + height;
		}

	}

}
