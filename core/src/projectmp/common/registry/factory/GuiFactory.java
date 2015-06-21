package projectmp.common.registry.factory;

import projectmp.common.inventory.gui.Gui;


public class GuiFactory extends Factory<Gui>{

	private static GuiFactory instance;

	private GuiFactory() {
	}

	public static GuiFactory instance() {
		if (instance == null) {
			instance = new GuiFactory();
			instance.loadResources();
		}
		return instance;
	}

	private void loadResources() {
		this.addBlueprint(new GuiBlueprint());
	}
	
}
