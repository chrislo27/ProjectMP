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

	protected void addGuiElements() {
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
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();
		container.render(main);
		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			exitScreen();
		}
	}

	protected void exitScreen() {
		main.setScreen(Main.MAINMENU);
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
