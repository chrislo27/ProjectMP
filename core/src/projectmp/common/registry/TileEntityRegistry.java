package projectmp.common.registry;

import projectmp.common.tileentity.TileEntity;


public class TileEntityRegistry extends RegistryBase<TileEntity>{

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
	
	private void loadResources() {

	}

}
