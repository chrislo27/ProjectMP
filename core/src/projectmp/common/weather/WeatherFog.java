package projectmp.common.weather;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.world.World;

public class WeatherFog extends Weather {

	public WeatherFog(int duration, World world) {
		super(duration, world);
	}

	@Override
	public void renderOverBackground(WorldRenderer renderer) {
		renderer.batch.setColor(232f / 255f, 232f / 255f, 232f / 255f,
				0.5f * getFadeCoefficient(5f));
		Main.fillRect(renderer.batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		renderer.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderOnWorld(WorldRenderer renderer) {
		renderer.batch.setColor(232f / 255f, 232f / 255f, 232f / 255f,
				0.75f * getFadeCoefficient(5f));
		Main.fillRect(renderer.batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		renderer.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void renderHUD(WorldRenderer renderer) {

	}

}
