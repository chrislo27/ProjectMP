package projectmp.client.settingsscreen;

import projectmp.client.Updateable;
import projectmp.client.ui.BackButton;
import projectmp.client.ui.BooleanButton;
import projectmp.client.ui.Button;
import projectmp.client.ui.ChoiceButton;
import projectmp.client.ui.LanguageButton;
import projectmp.client.ui.ResolutionButton;
import projectmp.client.ui.Slider;
import projectmp.client.ui.TextBox;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Settings.Resolution;
import projectmp.common.Translator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

public class SettingsScreen extends Updateable {

	public SettingsScreen(Main m) {
		super(m);

		addGuiElements();
	}

	private void addGuiElements() {
		container.elements.clear();
		container.elements.add(new BackButton(Settings.DEFAULT_WIDTH - 37,
				Settings.DEFAULT_HEIGHT - 37) {

			@Override
			public boolean onLeftClick() {
				exitScreen();
				return true;
			}
		});
		container.elements.add(new LanguageButton(5, 5));
		container.elements.add(music.setSlider(Settings.musicVolume));
		container.elements.add(sound.setSlider(Settings.soundVolume));
		container.elements.add(new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80, Gdx.graphics
				.getHeight() - 286, 160, 32, "menu.settings.showfps") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.showFPS = !Settings.showFPS;
				Settings.getPreferences().putBoolean("showFPS", Settings.showFPS).flush();
				return true;
			}
		}.setState(Settings.showFPS));
		container.elements.add(debug);
		container.elements.add(new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80, Gdx.graphics
				.getHeight() - 378, 160, 32, "menu.settings.vignette") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.showVignette = !Settings.showVignette;
				return true;
			}
		}.setState(Settings.showVignette));
		container.elements.add(usernameBox);
		container.elements.add(new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80, Gdx.graphics
				.getHeight() - 424, 160, 32, "menu.settings.smoothlighting") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.smoothLighting = !Settings.smoothLighting;
				return true;
			}
		}.setState(Settings.smoothLighting));

		container.elements.add(resolutions169);
		container.elements.add(resolutions43);
		container.elements.add(resolutions1610);
		container.elements.add(aspectRatio);
		container.elements.add(fullscreen);

		container.elements.add(new Button((Settings.DEFAULT_WIDTH / 2) - 290,
				Settings.DEFAULT_HEIGHT - 430, 100, 32, "menu.settings.apply") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();

				Resolution r = null;
				switch (aspectRatio.selection) {
				case 0: // 16:9
					r = Settings.get169ResolutionsList()[resolutions169.selection];
					break;
				case 1: // 4:3
					r = Settings.get43ResolutionsList()[resolutions43.selection];
					break;
				case 2: // 16:10
					r = Settings.get1610ResolutionsList()[resolutions1610.selection];
					break;
				}

				Settings.actualWidth = r.width;
				Settings.actualHeight = r.height;
				Settings.fullscreen = fullscreen.state;

				Gdx.graphics.setDisplayMode(Settings.actualWidth, Settings.actualHeight,
						Settings.fullscreen);

				return true;
			}
		});
	}

	private Slider music = new Slider((Settings.DEFAULT_WIDTH / 2) - 80,
			Settings.DEFAULT_HEIGHT - 192, 160, 32);
	private Slider sound = new Slider((Settings.DEFAULT_WIDTH / 2) - 80,
			Settings.DEFAULT_HEIGHT - 240, 160, 32);

	private TextBox usernameBox = new TextBox((Settings.DEFAULT_WIDTH / 2) - 80,
			Gdx.graphics.getHeight() - 144, 160, 32, "" + Main.username).setAllowDigits(true)
			.setAllowLetters(true).setAllowSpaces(true).setAllowSpecial(false)
			.setPasswordMode(false);

	private BooleanButton debug = new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80,
			Settings.DEFAULT_HEIGHT - 332, 160, 32, "menu.settings.debugmode") {

		@Override
		public boolean onLeftClick() {
			super.onLeftClick();
			Settings.debug = !Settings.debug;
			return true;
		}
	}.setState(Settings.debug);

	private ResolutionButton resolutions169 = new ResolutionButton(
			(Settings.DEFAULT_WIDTH / 2) - 320, Settings.DEFAULT_HEIGHT - 334, 160, 32,
			Settings.get169ResolutionsStrings()) {

		@Override
		public boolean visible() {
			return aspectRatio.selection == 0;
		}
	};

	private ResolutionButton resolutions43 = new ResolutionButton(
			(Settings.DEFAULT_WIDTH / 2) - 320, Settings.DEFAULT_HEIGHT - 334, 160, 32,
			Settings.get43ResolutionsStrings()) {

		@Override
		public boolean visible() {
			return aspectRatio.selection == 1;
		}
	};

	private ResolutionButton resolutions1610 = new ResolutionButton(
			(Settings.DEFAULT_WIDTH / 2) - 320, Settings.DEFAULT_HEIGHT - 334, 160, 32,
			Settings.get1610ResolutionsStrings()) {

		@Override
		public boolean visible() {
			return aspectRatio.selection == 2;
		}
	};

	private ChoiceButton aspectRatio = new ChoiceButton((Settings.DEFAULT_WIDTH / 2) - 320,
			Settings.DEFAULT_HEIGHT - 286, 160, 32, "menu.settings.aspectratio", new String[] {
					"menu.settings.aspectratio.169", "menu.settings.aspectratio.43",
					"menu.settings.aspectratio.1610" });

	private BooleanButton fullscreen = new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 320,
			Settings.DEFAULT_HEIGHT - 382, 160, 32, "menu.settings.fullscreen") {

		@Override
		public boolean onLeftClick() {
			super.onLeftClick();
			Settings.fullscreen = !Settings.fullscreen;
			return true;
		}
	}.setState(Settings.fullscreen);

	private boolean showRestartMsg = false;

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();
		container.render(main);

		if (showRestartMsg) {
			main.font.setColor(1, 0, 0, 1);
			main.font.draw(main.batch, "*",
					(Settings.DEFAULT_WIDTH / 2) - 100 - (main.font.getSpaceWidth() * 2),
					Settings.DEFAULT_HEIGHT - 144 + 20);

			main.font.setColor(1, 1, 1, 1);
			main.drawScaled(main.font, Translator.getMsg("menu.settings.requiresrestart"),
					(Settings.DEFAULT_WIDTH / 2), 100, 512, 0);
		}
		main.font.setColor(1, 1, 1, 1);
		main.font.draw(main.batch,
				Translator.getMsg("menu.settings.musicvol", (int) (music.slider * 100)),
				(Settings.DEFAULT_WIDTH / 2) + 80 + (main.font.getSpaceWidth()),
				Settings.DEFAULT_HEIGHT - 192 + 20);
		main.font.draw(main.batch,
				Translator.getMsg("menu.settings.soundvol", (int) (sound.slider * 100)),
				(Settings.DEFAULT_WIDTH / 2) + 80 + (main.font.getSpaceWidth()),
				Settings.DEFAULT_HEIGHT - 240 + 20);
		main.drawInverse(main.font,
				Translator.getMsg("menu.settings.username", (int) (sound.slider * 100)),
				(Settings.DEFAULT_WIDTH / 2) - 80, Settings.DEFAULT_HEIGHT - 144 + 23);
		main.drawCentered(main.font, Translator.getMsg("menu.settings.graphics"),
				(Settings.DEFAULT_WIDTH / 2) - 240, Settings.DEFAULT_HEIGHT - 232);

		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.R) && Settings.debug) {
			addGuiElements();
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			exitScreen();
		}

		debug.setState(Settings.debug);
	}

	private void exitScreen() {
		main.setScreen(Main.MAINMENU);
		Main.username = usernameBox.text;
		Settings.instance().save();
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		usernameBox.text = Main.username;
		pickCorrectResolution();
	}

	private void pickCorrectResolution() {
		fullscreen.state = Settings.fullscreen;
		for (int i = 0; i < Settings.get169ResolutionsList().length; i++) {
			Resolution r = Settings.get169ResolutionsList()[i];
			if (r.width == Gdx.graphics.getWidth() && r.height == Gdx.graphics.getHeight()) {
				aspectRatio.selection = 0;
				resolutions169.selection = i;
				return;
			}
		}
		for (int i = 0; i < Settings.get43ResolutionsList().length; i++) {
			Resolution r = Settings.get43ResolutionsList()[i];
			if (r.width == Gdx.graphics.getWidth() && r.height == Gdx.graphics.getHeight()) {
				aspectRatio.selection = 1;
				resolutions43.selection = i;
				return;
			}
		}
		for (int i = 0; i < Settings.get1610ResolutionsList().length; i++) {
			Resolution r = Settings.get1610ResolutionsList()[i];
			if (r.width == Gdx.graphics.getWidth() && r.height == Gdx.graphics.getHeight()) {
				aspectRatio.selection = 2;
				resolutions1610.selection = i;
				return;
			}
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
