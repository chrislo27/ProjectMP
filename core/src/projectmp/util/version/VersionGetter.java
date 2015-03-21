package projectmp.util.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import projectmp.Main;
import projectmp.Settings;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class VersionGetter {

	private static VersionGetter instance;

	private VersionGetter() {
	}

	public static VersionGetter instance() {
		if (instance == null) {
			instance = new VersionGetter();
			instance.loadResources();
		}
		return instance;
	}

	private void loadResources() {

	}

	/**
	 * NOTE: This method blocks until it fails or completes
	 */
	public void getVersionFromServer() {
		final String path = "https://raw.githubusercontent.com/chrislo27/VersionPlace/master/ProjectMP-version.txt";
		long start = System.currentTimeMillis();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new URL(path).openStream()));

			StringBuilder file = new StringBuilder();
			String inputline;
			while ((inputline = br.readLine()) != null)
				file.append(inputline);

			br.close();

			Main.logger.info("Finished getting version, took "
					+ (System.currentTimeMillis() - start) + " ms");

			
			Main.githubVersion = file.toString();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			Main.logger.error("Failed to parse/get latest version info", e);
		}
	}
}
