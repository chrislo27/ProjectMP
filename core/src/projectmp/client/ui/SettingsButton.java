package projectmp.client.ui;

import projectmp.client.transition.FadeIn;
import projectmp.client.transition.FadeOut;
import projectmp.common.Main;
import projectmp.common.Translator;

import com.badlogic.gdx.graphics.Color;

public class SettingsButton extends Button {

	public SettingsButton(int x, int y) {
		super(x, y, 48, 48, null);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guisettings");
		if (this.main == null) this.main = main;
	}

	private Main main = null;

	@Override
	public boolean onLeftClick() {
		if (main == null) {
			return false;
		}
		main.setScreen(Main.SETTINGS);
		return true;
	}

}
