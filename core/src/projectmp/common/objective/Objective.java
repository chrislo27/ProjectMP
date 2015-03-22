package projectmp.common.objective;

import com.badlogic.gdx.Gdx;

public class Objective {

	public String id = null;
	private boolean completed = false;
	public long completedTime = -1;
	public float outTime = -1f;
	public boolean failed = false;

	public static final long showTimeWhenCompleted = 2500;

	public Objective(String id) {
		this.id = id;
	}

	public Objective complete(boolean fail) {
		if (completed) return this;

		completedTime = System.currentTimeMillis();
		completed = true;
		failed = fail;
		outTime = 0.9999f;

		return this;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void update() {
		if (outTime < 1) {
			if (!isCompleted()) {
				outTime += (1f - outTime) * Gdx.graphics.getDeltaTime() * 4f;
			} else {
				if (System.currentTimeMillis() - completedTime >= (showTimeWhenCompleted / 4f) * 3) {
					outTime += (-1f - outTime) * Gdx.graphics.getDeltaTime() * 4f;
				}
			}

			if (outTime > 1) outTime = 1;
			if (outTime < -1f) outTime = -1f;
		}
	}

}
