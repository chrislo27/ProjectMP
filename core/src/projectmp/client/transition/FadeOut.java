package projectmp.client.transition;

import projectmp.common.Main;
import projectmp.common.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class FadeOut implements Transition {

	public FadeOut() {
		this(Color.BLACK, 1);
	}

	public FadeOut(Color c, float time) {
		color = c;
		timeleft = time;
	}

	float timeleft = 1f;
	Color color = Color.BLACK;

	@Override
	public boolean finished() {
		return timeleft <= 0;
	}

	@Override
	public void render(Main main) {
		main.batch.setColor(color.r, color.g, color.b, timeleft);
		Main.fillRect(main.batch, 0, 0, Settings.DEFAULT_WIDTH, Gdx.graphics.getHeight());
		main.batch.setColor(Color.WHITE);
		if (timeleft > 0) {
			timeleft -= Gdx.graphics.getRawDeltaTime();
			if (timeleft < 0) timeleft = 0;
		}
	}

	@Override
	public void tickUpdate(Main main) {

	}
}
