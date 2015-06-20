package projectmp.common.registry;

import projectmp.common.weather.Weather;
import projectmp.common.weather.WeatherDustStorm;
import projectmp.common.weather.WeatherFog;
import projectmp.common.weather.WeatherHail;
import projectmp.common.weather.WeatherRain;
import projectmp.common.weather.WeatherThundering;


public class WeatherRegistry extends RegistryBase<Weather>{
	
	private static WeatherRegistry instance;

	private WeatherRegistry() {
	}

	public static WeatherRegistry instance() {
		if (instance == null) {
			instance = new WeatherRegistry();
			instance.loadResources();
		}
		return instance;
	}

	private void loadResources() {
		register(WeatherDustStorm.class, "dustStorm");
		register(WeatherFog.class, "fog");
		register(WeatherHail.class, "hail");
		register(WeatherRain.class, "rain");
		register(WeatherThundering.class, "thundering");
	}
	
}
