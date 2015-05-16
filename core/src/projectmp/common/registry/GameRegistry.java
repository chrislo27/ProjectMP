package projectmp.common.registry;

import java.util.HashMap;

import projectmp.common.entity.Entity;
import projectmp.common.tileentity.TileEntity;


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
	
	private void loadResources() {
		// built-in game objects to be registered here
		registerTileEntity(TileEntity.class, "tileEntity");
	}
	
	public static void registerTileEntity(Class<? extends TileEntity> clazz, String name){
		instance().tileEntityRegistry.register(clazz, name);
	}
	
	public static void registerEntity(Class<? extends Entity> clazz, String name){
		instance().entityRegistry.register(clazz, name);
	}
	
}
