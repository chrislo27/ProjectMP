package projectmp.common.inventory.container;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Slot;
import projectmp.common.tileentity.IInventory;
import projectmp.common.tileentity.TileEntity;

import com.badlogic.gdx.utils.Array;

/**
 * A container connects the player's inventory and a tile entity's inventory to a GUI. It has the slot instances.
 * <br>
 * This is re-instantiated whenever a player opens an inventory on both client (in the Gui class) and server.
 * <br>
 * All sub-classes should add slots in their constructor and bind the tile entity (if needed).
 *
 */
public class Container {

	public Array<Slot> slots = new Array<>();
	
	public Container(){
		
	}
	
}
