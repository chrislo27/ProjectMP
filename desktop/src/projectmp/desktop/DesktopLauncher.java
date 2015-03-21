package projectmp.desktop;

import projectmp.Main;
import projectmp.Settings;
import projectmp.util.Logger;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	private static Logger logger;
	
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "";
		config.width = Settings.DEFAULT_WIDTH;
		config.height = Settings.DEFAULT_HEIGHT;
		config.foregroundFPS = Main.MAX_FPS;
		config.backgroundFPS = Main.MAX_FPS;
		config.resizable = false;
		config.vSyncEnabled = true;
		
		config.addIcon("images/icon/icon32.png", FileType.Internal);
		config.addIcon("images/icon/icon16.png", FileType.Internal);
		config.addIcon("images/icon/icon128.png", FileType.Internal);
		
		logger = new Logger("", com.badlogic.gdx.utils.Logger.DEBUG);
		new GameLwjglApp(new Main(logger), config, logger);
	}
}
