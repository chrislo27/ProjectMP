package projectmp.common.weather;

import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class WeatherPrecipitation extends Weather {

	public WeatherPrecipitation(int duration, World world) {
		super(duration, world);
	}

}
