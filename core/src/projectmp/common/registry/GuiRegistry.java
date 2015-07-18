package projectmp.common.registry;

import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Gui;
import projectmp.common.registry.handler.IGuiHandler;
import projectmp.common.registry.handler.StockGuiHandler;
import projectmp.common.tileentity.HasInventory;
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
		addGuiHandler(new StockGuiHandler());
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
	
	public HasInventory getInventory(String id, World world, int x, int y){
		HasInventory inv = null;
		for(int i = 0; i < handlers.size; i++){
			if((inv = handlers.get(i).getInventoryHolder(id, world, x, y)) != null) return inv;
		}
		
		return null;
	}
	
}
