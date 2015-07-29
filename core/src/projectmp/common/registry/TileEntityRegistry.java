package projectmp.common.registry;

import projectmp.common.registry.classmap.ClassMap;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.tileentity.TileEntityTestBattery;
import projectmp.common.tileentity.TileEntityTestGenerator;



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
		registerTileEntity(TileEntityTestBattery.class, "testBattery");
		registerTileEntity(TileEntityTestGenerator.class, "testGenerator");
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
