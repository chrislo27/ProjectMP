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
	
	private static final String[] resolutions43 = new String[]{"640480", "800600", "1024x768", "1152x864", "1280x960"};
	private static final String[] resolutions169 = new String[]{"1176x664", "1280x720", "1360x768", "1366x768", "1600x900", "1768x992", "1920x1080"};
	private static final String[] resolutions1610 = new String[]{"1280x800", "1440x900", "1600x1024", "1680x750"};
	
	public static String[] get43ResolutionsList(){
		return resolutions43;
	}
	
	public static String[] get169ResolutionsList(){
		return resolutions169;
	}
	
	public static String[] get1610ResolutionsList(){
		return resolutions1610;
	}

}
