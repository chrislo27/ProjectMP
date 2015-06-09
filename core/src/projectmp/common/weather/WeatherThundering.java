package projectmp.common.weather;

import com.badlogic.gdx.graphics.g2d.Batch;

import projectmp.client.WorldRenderer;
import projectmp.common.world.World;

public class WeatherThundering extends WeatherRain {

	public WeatherThundering(int duration, World world) {
		super(duration, world);
	}

	@Override
	public void renderOverBackground(WorldRenderer renderer) {
		super.renderOverBackground(renderer);
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();
	}

	@Override
	public void renderOnWorld(WorldRenderer renderer) {
		super.renderOnWorld(renderer);
	}

	@Override
	public void renderHUD(WorldRenderer renderer) {
		super.renderHUD(renderer);
	}

}
