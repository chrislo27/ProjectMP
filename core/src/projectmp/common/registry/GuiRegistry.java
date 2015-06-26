package projectmp.common.registry;

import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Gui;
import projectmp.common.inventory.gui.GuiChessBoard;
import projectmp.common.inventory.gui.GuiPlayerInventory;
import projectmp.common.tileentity.HasInventory;
import projectmp.common.util.Utils;
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
	
	public Inventory getInventory(String id, World world, int x, int y){
		Inventory inv = null;
		for(int i = 0; i < handlers.size; i++){
			if((inv = handlers.get(i).getInventoryObject(id, world, x, y)) != null) return inv;
		}
		
		return null;
	}
	
	private static class GuiHandler implements IGuiHandler{

		@Override
		public Gui getGuiObject(String id, World world, InventoryPlayer player, int x, int y) {
			switch(id){
			case("playerInv"):
				return new GuiPlayerInventory(world, player, id, x, y);
			case("chessboard"):
				return new GuiChessBoard(world, player, id, x, y);
			default:
				return null;
			}
		}

		@Override
		public Inventory getInventoryObject(String id, World world, int x, int y) {
			switch(id){
			case("playerInv"):
				return ((EntityPlayer) (world.getEntityByUUID(Utils.packLong(x, y)))).getInventoryObject();
			case("chessboard"):
				return ((HasInventory) (world.getBlock(x, y))).getInventoryObject();
			default:
				return null;
			}
		}
		
	}
	
}
