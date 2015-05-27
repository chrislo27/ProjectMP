package projectmp.common.weather;

import com.badlogic.gdx.graphics.g2d.Batch;

import projectmp.common.world.World;


public class WeatherThundering extends WeatherRain{

	public WeatherThundering(int duration, World world) {
		super(duration, world);
	}
	
	@Override
	public void renderOverBackground(Batch batch) {
		super.renderOverBackground(batch);
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();
	}

	@Override
	public void renderOnWorld(Batch batch) {
		super.renderOnWorld(batch);
	}

	@Override
	public void renderHUD(Batch batch) {
		super.renderHUD(batch);
	}

}
