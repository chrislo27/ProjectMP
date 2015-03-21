package stray.ai;

import stray.Main;
import stray.entity.Entity;

public class AIDumbEnemy extends BaseAI {

	public AIDumbEnemy(Entity e) {
		super(e);
	}

	/**
	 * false = left
	 */
	boolean direction = Main.getRandom().nextBoolean();

	@Override
	public void tickUpdate() {

	}

	@Override
	public void renderUpdate() {
		if (entity.getBlockCollidingLeft() != null && entity.getBlockCollidingRight() != null) {
			return;
		}

		if (direction) {
			entity.moveRight();
		} else {
			entity.moveLeft();
		}

		if (direction) {
			if (entity.getBlockCollidingRight() != null || entity.getEntityCollidingRight() != null) direction = false;
		} else {
			if (entity.getBlockCollidingLeft() != null || entity.getEntityCollidingLeft() != null) direction = true;
		}
	}

}
