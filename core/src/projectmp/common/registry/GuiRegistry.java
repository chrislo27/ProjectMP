package projectmp.common.registry;

import projectmp.common.inventory.gui.Gui;
import projectmp.common.registry.factory.Factory;
import projectmp.common.registry.factory.Factory.Blueprint;


public class GuiRegistry{

	public static class GuiBlueprint implements Blueprint<Gui>{

		@Override
		public Gui manufacture(String key) {

			switch(key){
			
			default:
				return null;
			}
			
		}
		
	}
	
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

	private Factory<Gui> guiFactory = new Factory<>();
	
	private void loadResources() {
		registerGuiBlueprint(new GuiBlueprint());
	}
	
	public Gui createNewGuiObject(String key){
		return guiFactory.manufacture(key);
	}
	
	public void registerGuiBlueprint(Blueprint<Gui> bp){
		guiFactory.addBlueprint(bp);
	}
	
}
