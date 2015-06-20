package projectmp.common.registry;

import projectmp.common.inventory.gui.Gui;


public class GuiRegistry extends RegistryBase<Gui>{
	
	private static GuiRegistry instance;

	private GuiRegistry() {
	}

	public static GuiRegistry instance() {
		if (instance == null) {
			instance = new GuiRegistry();
			instance.loadResources();
		}
		return instance;
	}

	private void loadResources() {
		
	}
	
}
