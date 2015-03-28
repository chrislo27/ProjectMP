package projectmp.client;

import projectmp.client.ui.Button;
import projectmp.common.Main;
import projectmp.common.Settings;


public class ErrorScreen extends MessageScreen{

	public ErrorScreen(Main m) {
		super(m);
		
		container.elements.add(new Button((Settings.DEFAULT_WIDTH / 2) - 80, 128, 160, 32,
				"menu.backmainmenu") {

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.MAINMENU);
				return true;
			}

			@Override
			public boolean visible() {
				return true;
			}
		});
	}

}
