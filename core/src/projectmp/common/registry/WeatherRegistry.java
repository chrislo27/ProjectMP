package projectmp.common.registry;

import projectmp.common.registry.classmap.ClassMap;
import projectmp.common.weather.Weather;
import projectmp.common.weather.WeatherDustStorm;
import projectmp.common.weather.WeatherFog;
import projectmp.common.weather.WeatherHail;
import projectmp.common.weather.WeatherRain;
import projectmp.common.weather.WeatherThundering;


public class WeatherRegistry {
	
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

	private ClassMap<Weather> weatherClassMap = new ClassMap<>();
	
	private void loadResources() {
		registerWeather(WeatherDustStorm.class, "dustStorm");
		registerWeather(WeatherFog.class, "fog");
		registerWeather(WeatherHail.class, "hail");
		registerWeather(WeatherRain.class, "rain");
		registerWeather(WeatherThundering.class, "thundering");
	}

	public void registerWeather(Class<? extends Weather> clazz, String key) {
		weatherClassMap.register(clazz, key);
	}

	public String getWeatherKey(Class<? extends Weather> clazz) {
		return weatherClassMap.getKey(clazz);
	}

	public Class<? extends Weather> getWeatherClass(String key) {
		return weatherClassMap.getValue(key);
	}
	
}
