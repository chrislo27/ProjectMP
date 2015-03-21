package projectmp.util.render;

import projectmp.Main;
import projectmp.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class RandomText {

	public static void render(Main main, String text, int chance, int maxrender) {
		if (text != null) {
			float height = main.font.getBounds(text).height;
			for (int i = 0; i < maxrender; i++) {
				if (MathUtils.random(1, chance) != 1) continue;
				main.drawCentered(
						text,
						MathUtils.random(1, Settings.DEFAULT_WIDTH - 1),
						MathUtils.random(Math.round(height),
								Math.round(Gdx.graphics.getHeight() - height)));
			}
		}
	}

	/**
	 * synthetic method, turns chance into (10 + fps + (fps / 2))
	 * 
	 * @param main
	 * @param text
	 * @param maxrender
	 */
	public static void render(Main main, String text, int maxrender) {
		render(main, text,
				10 + Gdx.graphics.getFramesPerSecond() + (Gdx.graphics.getFramesPerSecond() / 2),
				maxrender);
	}

}
