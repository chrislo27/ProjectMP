package projectmp.common.weather;

import com.badlogic.gdx.graphics.g2d.Batch;

import projectmp.common.world.World;


public class WeatherFog extends Weather {

	public WeatherFog(int duration, World world) {
		super(duration, world);
	}

	@Override
	public void renderOverBackground(Batch batch) {
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderOnWorld(Batch batch) {
	}

	@Override
	public void renderHUD(Batch batch) {
	}

}
