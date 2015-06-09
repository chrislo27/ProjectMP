package projectmp.common.weather;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;

public abstract class Weather {

	/**
	 * the time remaining in ticks. updated by world in its own tickupdate method
	 */
	protected int ticksRemaining;

	/**
	 * the length of the weather
	 */
	protected int originalDuration;

	protected World world;

	public Weather(int duration, World world) {
		originalDuration = duration;
		ticksRemaining = duration;

		this.world = world;
	}

	public abstract void renderOverBackground(WorldRenderer renderer);

	public abstract void tickUpdate();

	public abstract void renderOnWorld(WorldRenderer renderer);

	public abstract void renderHUD(WorldRenderer renderer);

	public void onStart() {

	}

	public void onFinish() {

	}

	public void setTimeRemaining(int timeRemaining) {
		if (timeRemaining > originalDuration) {
			originalDuration = timeRemaining;
		}
		ticksRemaining = timeRemaining;
	}

	public int getTimeRemaining() {
		return ticksRemaining;
	}

	public int getTotalDuration() {
		return originalDuration;
	}

	public void tickDownTimeRemaining() {
		if (ticksRemaining == originalDuration) {
			onStart();
		}

		ticksRemaining--;

		if (ticksRemaining == 0) {
			onFinish();
		}
	}

	/**
	 * handy method that returns a coefficient you can use to fade in (for example alpha values)
	 * @return
	 */
	public float getFadeCoefficient(float secondsAway) {
		float returnValue = 1;
		int ticks = (int) (secondsAway * Main.TICKS);

		if (originalDuration - ticksRemaining <= ticks) {
			returnValue = ((originalDuration - ticksRemaining) * 1f / ticks);
		}
		if (ticksRemaining <= ticks) {
			returnValue = ticksRemaining * 1f / ticks;
		}

		return returnValue;
	}

}
