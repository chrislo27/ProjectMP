package projectmp.common.registry;

import projectmp.common.entity.Entity;
import projectmp.common.registry.instantiator.ClassMap;
import projectmp.common.tileentity.TileEntity;



public class TileEntityRegistry {

	private static TileEntityRegistry instance;

	private TileEntityRegistry() {
	}

	public static TileEntityRegistry instance() {
		if (instance == null) {
			instance = new TileEntityRegistry();
			instance.loadResources();
		}
		return instance;
	}
	
	private ClassMap<TileEntity> tileEntityClassMap = new ClassMap<>();
	
	private void loadResources() {
		
	}
	
	public void registerTileEntity(Class<? extends TileEntity> clazz, String key){
		tileEntityClassMap.register(clazz, key);
	}
	
	public String getTileEntityKey(Class<? extends TileEntity> clazz){
		return tileEntityClassMap.getKey(clazz);
	}
	
	public Class<? extends TileEntity> getTileEntityClass(String key){
		return tileEntityClassMap.getValue(key);
	}

}
