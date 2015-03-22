package client.ui;

import common.Main;

public interface GuiElement {

	public void render(Main main);

	public boolean visible();

	public int getX();

	public int getY();

	public int getWidth();

	public int getHeight();
	

	/**
	 * 
	 * @return true if handled
	 */
	public boolean onLeftClick();

	/**
	 * 
	 * @return true if handled
	 */
	public boolean onRightClick();
}
