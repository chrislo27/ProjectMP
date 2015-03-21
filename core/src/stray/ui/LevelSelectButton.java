package stray.ui;

import stray.Main;


public abstract class LevelSelectButton extends Button{

	public LevelSelectButton(int x, int y) {
		super(x, y, 48, 48, null);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guilevelselect");
	}

	@Override
	public abstract boolean onLeftClick();
	
	@Override
	public boolean onRightClick() {
		return false;
	}
	
}
