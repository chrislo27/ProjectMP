package client.ui;

import common.Main;


public abstract class RetryLevelButton extends Button{

	public RetryLevelButton(int x, int y) {
		super(x, y, 48, 48, null);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guiretry");
	}

	@Override
	public abstract boolean onLeftClick();
	
	@Override
	public boolean onRightClick() {
		return false;
	}
	
}
