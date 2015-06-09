package projectmp.client.ui;

import projectmp.common.Main;

public abstract class UiElement {

	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public abstract void render(Main main);

	public abstract boolean visible();

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * @return true if handled
	 */
	public abstract boolean onLeftClick();

	/**
	 * 
	 * @return true if handled
	 */
	public abstract boolean onRightClick();

	public boolean onKeyTyped(char key) {
		return false;
	}
}
