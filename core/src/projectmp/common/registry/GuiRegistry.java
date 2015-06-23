package projectmp.common.registry;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Gui;
import projectmp.common.inventory.gui.GuiTest;
import projectmp.common.world.World;

import com.badlogic.gdx.utils.Array;


public class GuiRegistry {

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

	private Array<IGuiHandler> handlers = new Array<>();
	
	private void loadResources() {
		addGuiHandler(new GuiRegistry.GuiHandler());
	}
	
	public void addGuiHandler(IGuiHandler handler){
		handlers.add(handler);
	}
	
	public Gui createNewGuiObject(String id, World world, InventoryPlayer player, int x, int y){
		Gui g = null;
		for(int i = 0; i < handlers.size; i++){
			if((g = handlers.get(i).getGuiObject(id, world, player, x, y)) != null) return g;
		}
		
		return null;
	}
	
	private static class GuiHandler implements IGuiHandler{

		@Override
		public Gui getGuiObject(String id, World world, InventoryPlayer player, int x, int y) {
			switch(id){
			case("testGuiDoNotTouchBuleah"):
				return new GuiTest(player);
			default:
				return null;
			}
		}
		
	}
	
}
