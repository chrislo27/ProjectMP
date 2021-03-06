package projectmp.client.settingsscreen;

import projectmp.client.ui.BooleanButton;
import projectmp.client.ui.Button;
import projectmp.client.ui.TextBox;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;

import com.badlogic.gdx.Gdx;

public class GeneralSettingsScreen extends SettingsScreen {

	private TextBox usernameBox;

	private BooleanButton debug;

	public GeneralSettingsScreen(Main m) {
		super(m);
	}

	@Override
	protected void addGuiElements() {
		super.addGuiElements();

		usernameBox = new TextBox((Settings.DEFAULT_WIDTH / 2) - 160,
				Gdx.graphics.getHeight() - 144, 320, 32, "" + Main.username).setAllowDigits(true)
				.setAllowLetters(true).setAllowSpaces(true).setAllowSpecial(false)
				.setPasswordMode(false);

		container.elements.add(new Button((Settings.DEFAULT_WIDTH / 2) - 160, Gdx.graphics
				.getHeight() - 192, 320, 32, "menu.settings.graphics") {

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.GRAPHICSSETTINGS);
				return true;
			}
		});

		container.elements.add(new Button((Settings.DEFAULT_WIDTH / 2) - 160, Gdx.graphics
				.getHeight() - 240, 320, 32, "menu.settings.audio") {

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.AUDIOSETTINGS);
				return true;
			}
		});

		debug = new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 160, Settings.DEFAULT_HEIGHT - 332,
				320, 32, "menu.settings.debugmode") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.debug = !Settings.debug;
				return true;
			}
		}.setState(Settings.debug);

		container.elements.add(new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 160, Gdx.graphics
				.getHeight() - 286, 320, 32, "menu.settings.showfps") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.showFPS = !Settings.showFPS;
				Settings.getPreferences().putBoolean("showFPS", Settings.showFPS).flush();
				return true;
			}
		}.setState(Settings.showFPS));
		container.elements.add(debug);
		container.elements.add(usernameBox);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		main.batch.begin();
		main.font.setColor(1, 1, 1, 1);

		main.drawInverse(main.font, Translator.getMsg("menu.settings.username"),
				(Settings.DEFAULT_WIDTH / 2) - 160, Settings.DEFAULT_HEIGHT - 144 + 23);

		main.font.setScale(2);
		main.font.setColor(1, 1, 1, 1);
		main.drawCentered(main.font, Translator.getMsg("menu.settings.general"),
				(Settings.DEFAULT_WIDTH / 2), Settings.DEFAULT_HEIGHT - 32);
		main.font.setScale(1);

		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		super.renderUpdate();
		debug.setState(Settings.debug);
	}

	@Override
	protected void exitScreen() {
		Main.username = usernameBox.text;
		super.exitScreen();
	}

	@Override
	public void show() {
		usernameBox.text = Main.username;
	}

}
