package projectmp.client.settingsscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import projectmp.client.Updateable;
import projectmp.client.ui.BooleanButton;
import projectmp.client.ui.Button;
import projectmp.client.ui.ChoiceButton;
import projectmp.client.ui.ResolutionButton;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;
import projectmp.common.Settings.Resolution;

public class GraphicsSettingsScreen extends SettingsScreen {

	private ResolutionButton resolutions169;
	private ResolutionButton resolutions43;
	private ResolutionButton resolutions1610;
	private ChoiceButton aspectRatio;
	private BooleanButton fullscreen;

	public GraphicsSettingsScreen(Main m) {
		super(m);
	}

	@Override
	protected void addGuiElements() {
		super.addGuiElements();

		container.elements.add(resolutions169 = new ResolutionButton(
				(Settings.DEFAULT_WIDTH / 2) - 320, Settings.DEFAULT_HEIGHT - 334, 160, 32,
				Settings.get169ResolutionsStrings()) {

			@Override
			public boolean visible() {
				return aspectRatio.selection == 0;
			}
		});
		container.elements.add(resolutions43 = new ResolutionButton(
				(Settings.DEFAULT_WIDTH / 2) - 320, Settings.DEFAULT_HEIGHT - 334, 160, 32,
				Settings.get43ResolutionsStrings()) {

			@Override
			public boolean visible() {
				return aspectRatio.selection == 1;
			}
		});
		container.elements.add(resolutions1610 = new ResolutionButton(
				(Settings.DEFAULT_WIDTH / 2) - 320, Settings.DEFAULT_HEIGHT - 334, 160, 32,
				Settings.get1610ResolutionsStrings()) {

			@Override
			public boolean visible() {
				return aspectRatio.selection == 2;
			}
		});
		container.elements.add(aspectRatio = new ChoiceButton((Settings.DEFAULT_WIDTH / 2) - 320,
				Settings.DEFAULT_HEIGHT - 286, 160, 32, "menu.settings.aspectratio", new String[] {
						"menu.settings.aspectratio.169", "menu.settings.aspectratio.43",
						"menu.settings.aspectratio.1610" }));
		container.elements.add(fullscreen = new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 320,
				Settings.DEFAULT_HEIGHT - 382, 160, 32, "menu.settings.fullscreen") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.fullscreen = !Settings.fullscreen;
				return true;
			}
		}.setState(Settings.fullscreen));

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
		container.elements.add(new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80,
				Settings.DEFAULT_HEIGHT - 334, 160, 32, "menu.settings.vignette") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.showVignette = !Settings.showVignette;
				return true;
			}
		}.setState(Settings.showVignette));
		container.elements.add(new BooleanButton((Settings.DEFAULT_WIDTH / 2) - 80,
				Settings.DEFAULT_HEIGHT - 382, 160, 32, "menu.settings.smoothlighting") {

			@Override
			public boolean onLeftClick() {
				super.onLeftClick();
				Settings.smoothLighting = !Settings.smoothLighting;
				return true;
			}
		}.setState(Settings.smoothLighting));
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		main.batch.begin();
		main.font.setColor(1, 1, 1, 1);

		main.font.setScale(2);
		main.drawCentered(main.font, Translator.getMsg("menu.settings.graphics"),
				(Settings.DEFAULT_WIDTH / 2), Settings.DEFAULT_HEIGHT - 32);
		main.font.setScale(1);

		main.drawCentered(main.font, Translator.getMsg("menu.settings.graphics.resolution"),
				(Settings.DEFAULT_WIDTH / 2) - 240, Settings.DEFAULT_HEIGHT - 226);

		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.setScreen(Main.SETTINGS);
		}
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
	public void show() {
		pickCorrectResolution();
	}

	@Override
	protected void exitScreen() {
		super.exitScreen();
		main.setScreen(Main.SETTINGS);
	}

}
