package projectmp;

import com.badlogic.gdx.InputProcessor;

public class HelpInputProcessor implements InputProcessor {

	Main main;
	HelpScreen screen;

	public HelpInputProcessor(Main m, HelpScreen s) {
		main = m;
		screen = s;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (screen.konamisuccess < screen.konami.length) {
			if (keycode == screen.konami[screen.konamisuccess]) {
				screen.konamisuccess++;
				return true;
			} else {
				screen.konamisuccess = 0;
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
