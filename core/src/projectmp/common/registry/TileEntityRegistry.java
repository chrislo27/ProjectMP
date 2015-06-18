package projectmp.common.registry;

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

	private RegistryMap<TileEntity> tileEntityRegistry = new RegistryMap<>();
	
	private void loadResources() {

	}

	public void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
		tileEntityRegistry.register(clazz, name);
	}

	public RegistryMap<TileEntity> getTileEntityRegistry() {
		return tileEntityRegistry;
	}

}
