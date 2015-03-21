package stray;

import stray.transition.FadeIn;
import stray.transition.FadeOut;
import stray.ui.BooleanButton;
import stray.ui.Button;
import stray.ui.ChoiceButton;
import stray.ui.BackButton;
import stray.util.Difficulty;
import stray.util.render.SpaceBackground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class NewGameScreen extends Updateable {

	private ChoiceButton difficulty = new ChoiceButton((Settings.DEFAULT_WIDTH / 2) - 105, 64,
			210, 32, "menu.difficultyselect", new String[] { "menu.difficulty.easy",
					"menu.difficulty.normal", "menu.difficulty.hard" });

	public NewGameScreen(Main m) {
		super(m);

		difficulty.selection = Difficulty.NORMAL_ID;

		container.elements.add(difficulty);
		container.elements.add(new Button((Settings.DEFAULT_WIDTH / 2) - 80, 150, 160, 32,
				"menu.start") {

			@Override
			public boolean onLeftClick() {
				main.progress.clear();
				main.progress.putInteger("difficulty", difficulty.selection);
				main.progress.flush();

				main.getPref("settings").putBoolean("saveExists", true).flush();

				Main.LEVELSELECT.offset = 0;
				Main.LEVELSELECT.wanted = 0;
				Main.LEVELSELECT.velocity = 0;

				main.transition(new FadeIn(Color.BLACK, 0.5f), new FadeOut(Color.BLACK, 0.5f),
						Main.LEVELSELECT);

				Runtime.getRuntime().gc();

				return true;
			}

		});
		container.elements.add(new BackButton(Settings.DEFAULT_WIDTH - 37, Gdx.graphics
				.getHeight() - 37) {

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.MAINMENU);
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0.909803f, 0.909803f, 0.909803f, 1);
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();

		SpaceBackground.instance().render(main);

		main.font.setColor(Color.WHITE);
		Difficulty diff = Difficulty.get().get(difficulty.selection);
		main.drawCentered(Translator.getMsg(difficulty.choices.get(difficulty.selection))
				+ ": " + diff.damageMultiplier + "x " + Translator.getMsg("menu.difficultysummary"), Settings.DEFAULT_WIDTH / 2, Gdx.graphics.getHeight() / 2 - 7);

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
		difficulty.selection = Difficulty.NORMAL_ID;
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
