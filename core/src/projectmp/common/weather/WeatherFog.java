package projectmp.common.weather;

import com.badlogic.gdx.graphics.g2d.Batch;

import projectmp.client.WorldRenderer;
import projectmp.common.world.World;


public class WeatherFog extends Weather {

	public WeatherFog(int duration, World world) {
		super(duration, world);
	}

	@Override
	public void renderOverBackground(WorldRenderer renderer) {
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderOnWorld(WorldRenderer renderer) {
	}

	@Override
	public void renderHUD(WorldRenderer renderer) {
	}

}
