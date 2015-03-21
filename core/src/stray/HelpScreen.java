package stray;

import stray.util.Utils;
import stray.util.render.SpaceBackground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;

public class HelpScreen extends Updateable {

	public HelpScreen(Main m) {
		super(m);
		prepare();
	}

	private void prepare() {
		things.clear();

		things.add("_CONTROLS");
		things.add("");
		things.add("WASD / ARROW KEYS -> move character");
		things.add("F12 -> debug mode toggle");
		things.add("SCROLL -> scroll selected on hotbar");
		things.add("1234567890 -> hotbar slot select");
		// ------------------------------------------
		things.add("_CREDITS");
		things.add("");
		things.add("Programming");
		things.add("chrislo27");
		things.add("");
		things.add("Art");
		things.add("chrislo27");
		// ------------------------------------------
		things.add("_MUSIC CREDITS");
		things.add("");
		things.add("[insert music here]");
		things.add("Kevin MacLeod (incompetech.com)");
		things.add("\"Licensed under Creative Commons: By Attribution 3.0\"");
		things.add("\"http://creativecommons.org/licenses/by/3.0/\"");
	}

	Array<String> things = new Array<String>();

	final int[] konami = new int[] { Keys.UP, Keys.UP, Keys.DOWN, Keys.DOWN, Keys.LEFT, Keys.RIGHT,
			Keys.LEFT, Keys.RIGHT, Keys.B, Keys.A };
	int konamisuccess = 0;

	@Override
	public void render(float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		main.batch.begin();
		SpaceBackground.instance().render(main);
		main.font.setColor(Color.WHITE);

		int buffer = 0;
		for (int i = 0; i < things.size; i++) {
			if (things.get(i).startsWith("_")) {
				main.font.draw(main.batch, things.get(i).substring(1), 65,
						Main.convertY(75 + (i * 15) + (buffer * 15)));
				main.font.draw(main.batch, Utils.repeat("_", things.get(i).length() - 1), 65,
						Main.convertY(75 + (i * 15) + (buffer * 15)));
			} else main.font.draw(main.batch, things.get(i), 65,
					Main.convertY(75 + (i * 15) + (buffer * 15)));
			if (i + 1 < things.size) {
				if (things.get(i + 1).startsWith("_")) {
					buffer += 2;
				}
			}
		}

		main.batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			prepare();
		} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.setScreen(Main.MAINMENU);
		}

	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		InputMultiplexer p = main.getDefaultInput();
		p.addProcessor(new HelpInputProcessor(main, this));
		Gdx.input.setInputProcessor(p);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(main.getDefaultInput());
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
