package projectmp.client.ui;

import projectmp.common.Main;


public abstract class NextLevelButton extends Button{
	
	public NextLevelButton(int x, int y) {
		super(x, y, 48, 48, null);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guinextlevel");
	}

	@Override
	public abstract boolean onLeftClick();
	
	@Override
	public boolean onRightClick() {
		return false;
	}
}
