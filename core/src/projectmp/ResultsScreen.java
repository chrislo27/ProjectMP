package projectmp;

import projectmp.ui.LevelSelectButton;
import projectmp.ui.NextLevelButton;
import projectmp.ui.RetryLevelButton;
import projectmp.util.AssetMap;
import projectmp.util.DamageSource;
import projectmp.util.Utils;
import projectmp.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ResultsScreen extends Updateable {

	public ResultsScreen(Main m) {
		super(m);

		container.elements.add(new LevelSelectButton(Settings.DEFAULT_WIDTH / 2 - 24 - 8 - 48,
				Gdx.graphics.getHeight() / 4) {

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.LEVELSELECT);

				return true;
			}

		});

		container.elements.add(new RetryLevelButton(Settings.DEFAULT_WIDTH / 2 - 24, Gdx.graphics
				.getHeight() / 4) {

			@Override
			public boolean onLeftClick() {
				Main.LEVELSELECT.goToLevel(levelname);

				return true;
			}

		});

		container.elements.add(new NextLevelButton(Settings.DEFAULT_WIDTH / 2 - 24 + 8 + 48,
				Gdx.graphics.getHeight() / 4) {

			@Override
			public boolean onLeftClick() {
				Main.LEVELSELECT.goToLevel(Math.round(Main.LEVELSELECT.wanted));

				return true;
			}

		});

	}

	private String levelfile = null;
	private int levelname = 0;
	private int resultsPick = MathUtils.random(0, 3);
	private Array<DamageSource> deaths = new Array<DamageSource>(1);
	private Array<DeathIcon> icons = new Array<DeathIcon>(64);

	public ResultsScreen setData(String levelf, int levelid, 
			Array<DamageSource> deaths) {
		levelfile = levelf;
		levelname = levelid;
		this.deaths = deaths;
		groupDeaths();

		int old = resultsPick;
		while (resultsPick == old) {
			resultsPick = MathUtils.random(0, 3);
		}

		return this;
	}

	private void groupDeaths() {
		icons.clear();
		for (int i = 0; i < deaths.size; i++) {
			int following = 0;
			boolean added = false;

			if (i + 1 < deaths.size) {
				int cur = i + 1;
				while (cur < deaths.size) {
					if (deaths.get(cur) == deaths.get(i)) {
						following++;
					} else {
						icons.add(new DeathIcon(deaths.get(i), 1 + following));
						i += following;
						added = true;
						break;
					}
					cur++;
				}
			}

			if (!added) {
				icons.add(new DeathIcon(deaths.get(i), 1 + following));
				i += following;
			}
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();
		if (levelfile != null) {
			main.font.setColor(1, 1, 1, 1);
			main.font.setScale(2);
			main.drawCentered(Levels.getLevelName(levelname), Settings.DEFAULT_WIDTH / 2,
					Main.convertY(225));
			main.font.setScale(1);
			main.drawCentered(
					Translator.getMsg("menu.results.latesttime")
							+ Utils.formatMs(main.progress.getLong(levelfile + "-latesttime")),
					Settings.DEFAULT_WIDTH / 2, Main.convertY(275));
			main.drawCentered(
					Translator.getMsg("menu.results.besttime")
							+ Utils.formatMs(main.progress.getLong(levelfile + "-latesttime")),
					Settings.DEFAULT_WIDTH / 2, Main.convertY(300));

			main.drawCentered(Translator.getMsg("menu.results.deaths") + deaths.size,
					Settings.DEFAULT_WIDTH / 2, Main.convertY(325));

			if (deaths.size > 0) {
				renderDeaths(Settings.DEFAULT_WIDTH / 2 - ((icons.size / 2f) * 35),
						Main.convertY(375));
			}
		}

		container.render(main);
		main.batch.end();
	}

	private void renderDeaths(float x, float y) {

		for (int i = 0; i < icons.size; i++) {
			main.batch.draw(main.manager.get("images/ui/damage/" + icons.get(i).source.name
					+ ".png", Texture.class), x + (i * 35), y);
			if (icons.get(i).count > 1) {
				main.drawCentered("x" + icons.get(i).count, x + (i * 35) + 16.5f, y);
			}
		}

	}

	@Override
	public void renderUpdate() {
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

	public static class DeathIcon {

		DamageSource source = DamageSource.yourMother;
		int count = 1;

		public DeathIcon(DamageSource dmg, int group) {
			source = dmg;
			count = group;
		}
	}

}
