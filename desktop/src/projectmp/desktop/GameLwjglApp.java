package projectmp.desktop;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import projectmp.util.Logger;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class GameLwjglApp extends LwjglApplication {

	public GameLwjglApp(ApplicationListener listener, LwjglApplicationConfiguration config, Logger log) {
		super(listener, config);
		logger = log;
	}

	private SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
	private Logger logger;

	public Logger getLogger() {
		return logger;
	}

	@Override
	public void debug(String tag, String message) {
		if (logLevel >= LOG_DEBUG) {
			logger.debug((tag != null ? tag : "") + message);
		}

	}

	@Override
	public void debug(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) {
			logger.debug((tag != null ? tag : "") + message);
			exception.printStackTrace(System.out);
		}

	}

	@Override
	public void log(String tag, String message) {
		if (logLevel >= LOG_INFO) {
			logger.info((tag != null ? tag : "") + message);
		}

	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) {
			logger.info((tag != null ? tag : "") + message);
			exception.printStackTrace(System.out);
		}

	}

	@Override
	public void error(String tag, String message) {
		if (logLevel >= LOG_ERROR) {
			logger.debug((tag != null ? tag : "") + message);
		}

	}

	@Override
	public void error(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) {
			logger.error((tag != null ? tag : "") + message, exception);
		}

	}

}
