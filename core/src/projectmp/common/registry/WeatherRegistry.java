package projectmp.common.registry;

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
	
	private RegistryMap<Weather> weatherRegistry = new RegistryMap<>();

	private void loadResources() {
		registerWeather(WeatherDustStorm.class, "dustStorm");
		registerWeather(WeatherFog.class, "fog");
		registerWeather(WeatherHail.class, "hail");
		registerWeather(WeatherRain.class, "rain");
		registerWeather(WeatherThundering.class, "thundering");
	}
	
	public void registerWeather(Class<? extends Weather> clazz, String name) {
		weatherRegistry.register(clazz, name);
	}
	
	public RegistryMap<Weather> getWeatherRegistry() {
		return weatherRegistry;
	}
	
}
