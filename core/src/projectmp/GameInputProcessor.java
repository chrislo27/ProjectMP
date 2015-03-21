package projectmp;

import projectmp.world.World;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class GameInputProcessor implements InputProcessor {

	Main main;
	World world;

	public GameInputProcessor(Main m, World w) {
		main = m;
		world = w;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case (Keys.ESCAPE):
			main.setConv(null);
			return true;
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
		if (main.getConv() != null) return false;

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
