package projectmp.common.registry;

import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityLiving;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.weather.Weather;
import projectmp.common.weather.WeatherDustStorm;
import projectmp.common.weather.WeatherFog;
import projectmp.common.weather.WeatherHail;
import projectmp.common.weather.WeatherRain;
import projectmp.common.weather.WeatherThundering;


public class GameRegistry {

	static{
		GameRegistry.instance();
	}
	
	private static GameRegistry instance;

	private GameRegistry() {
	}

	public static GameRegistry instance() {
		if (instance == null) {
			instance = new GameRegistry();
			instance.loadResources();
		}
		return instance;
	}

	private RegistryMap<TileEntity> tileEntityRegistry = new RegistryMap<>();
	private RegistryMap<Entity> entityRegistry = new RegistryMap<>();
	private RegistryMap<Weather> weatherRegistry = new RegistryMap<>();
	
	private void loadResources() {
		// built-in game objects to be registered here
		// mods can have their own registry and call the static methods at runtime
		
		// entities
		registerEntity(EntityLiving.class, "entityLiving");
		registerEntity(EntityPlayer.class, "player");
		
		// tile entities
		
		// weather types
		registerWeather(WeatherDustStorm.class, "dustStorm");
		registerWeather(WeatherFog.class, "fog");
		registerWeather(WeatherHail.class, "hail");
		registerWeather(WeatherRain.class, "rain");
		registerWeather(WeatherThundering.class, "thundering");
	}
	
	public static void registerTileEntity(Class<? extends TileEntity> clazz, String name){
		instance().tileEntityRegistry.register(clazz, name);
	}
	
	public static void registerEntity(Class<? extends Entity> clazz, String name){
		instance().entityRegistry.register(clazz, name);
	}
	
	public static void registerWeather(Class<? extends Weather> clazz, String name){
		instance().weatherRegistry.register(clazz, name);
	}
	
	public static RegistryMap<TileEntity> getTileEntityRegistry(){
		return instance().tileEntityRegistry;
	}
	
	public static RegistryMap<Entity> getEntityRegistry(){
		return instance().entityRegistry;
	}
	
	public static RegistryMap<Weather> getWeatherRegistry(){
		return instance().weatherRegistry;
	}
	
}
