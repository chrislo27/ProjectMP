package projectmp.client;

import projectmp.client.ui.UiContainer;
import projectmp.common.Main;

import com.badlogic.gdx.Screen;

public abstract class Updateable implements Screen {

	public Main main;
	public UiContainer container = new UiContainer();

	public Updateable(Main m) {
		main = m;
	}

	@Override
	public abstract void render(float delta);

	/**
	 * updates once a render call only if this screen is active
	 */
	public abstract void renderUpdate();

	public abstract void tickUpdate();

	/**
	 * x is 5
	 */
	public abstract void renderDebug(int starting);

	@Override
	public abstract void resize(int width, int height);

	@Override
	public abstract void show();

	@Override
	public abstract void hide();

	@Override
	public abstract void pause();

	@Override
	public abstract void resume();

	@Override
	public abstract void dispose();

	public int getDebugOffset() {
		return 0;
	}

	public void debug(String message) {
		Main.logger.debug(message);
	}

	public void debug(String message, Exception exception) {
		Main.logger.debug(message, exception);
	}

	public void info(String message) {
		Main.logger.info(message);
	}

	public void info(String message, Exception exception) {
		Main.logger.info(message, exception);
	}

	public void error(String message) {
		Main.logger.error(message);
	}

	public void error(String message, Throwable exception) {
		Main.logger.error(message, exception);
	}

	public void warn(String message) {
		Main.logger.warn(message);
	}

	public void warn(String message, Throwable exception) {
		Main.logger.warn(message, exception);
	}
	
	/**
	 * Called when scrolled. Negative amount means scrolled up.
	 * @param amount
	 */
	public boolean onScrolled(int amount){
		return false;
	}

}
