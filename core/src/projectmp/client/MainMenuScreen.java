package projectmp.client;

import java.io.IOException;

import projectmp.client.ui.BackButton;
import projectmp.client.ui.Button;
import projectmp.client.ui.SettingsButton;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public strictfp class MainMenuScreen extends Updateable {

	public MainMenuScreen(Main m) {
		super(m);

		container.elements.add(new Button((Settings.DEFAULT_WIDTH / 2) - 80, 64, 160, 32,
				"localhost connection") {

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.CONNECTING);
				Main.CONNECTING.connectTo("localhost", Settings.DEFAULT_PORT);
				return true;
			}
		});
		container.elements.add(new Button((Settings.DEFAULT_WIDTH / 2) - 80, 128, 160, 32,
				"menu.continue") {

			@Override
			public boolean onLeftClick() {
				return true;
			}

			@Override
			public boolean visible() {
				return hasSave;
			}
		});
		container.elements.add(new SettingsButton(5, 5));
		container.elements.add(new BackButton(Settings.DEFAULT_WIDTH - 37, Gdx.graphics
				.getHeight() - 37) {

			@Override
			public boolean onLeftClick() {
				Gdx.app.exit();
				System.exit(0);
				return true;
			}
		}.useExitTexture());
	}

	boolean hasSave = false;

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();

		main.font.setColor(Color.WHITE);
		main.font.setScale(2.5f);
		main.drawCentered(Translator.getMsg("gamename").toUpperCase(), Settings.DEFAULT_WIDTH / 2,
				Main.convertY(200));
		main.font.setScale(1);

		main.drawInverse(Main.version, Settings.DEFAULT_WIDTH - 5, 20);
		if (Main.githubVersion == null) {
			main.drawInverse(Translator.getMsg("menu.checkingversion"),
					Settings.DEFAULT_WIDTH - 5, 35);
		} else {
			if(Main.githubVersion.equals(Main.version)){
				main.font.setColor(0, 1, 0, 1);
				main.drawInverse(Translator.getMsg("menu.uptodate"), Settings.DEFAULT_WIDTH - 5,
						35);
				main.font.setColor(1, 1, 1, 1);
			}else{
				main.font.setColor(1, 0, 0, 1);
				main.drawInverse(Translator.getMsg("menu.newversion") + Main.githubVersion,
						Settings.DEFAULT_WIDTH - 5, 35);
				main.font.setColor(1, 1, 1, 1);
			}
		}
		main.drawInverse(Translator.getMsg("menu.player") + Main.username, Settings.DEFAULT_WIDTH - 5, 50);
		container.render(main);
		main.font.setColor(Color.WHITE);
		main.batch.setColor(1, 1, 1, 1);

		main.batch.end();
	}

	@Override
	public void tickUpdate() {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		hasSave = main.getPref("settings").getBoolean("saveExists", false);
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

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void renderUpdate() {

	}

}
