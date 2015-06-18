package projectmp.common.inventory.container;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Slot;
import projectmp.common.tileentity.IInventory;
import projectmp.common.tileentity.TileEntity;

import com.badlogic.gdx.utils.Array;

/**
 * A container connects the player's inventory and a tile entity's inventory to a GUI. It has the slot instances.
 * <br>
 * This is re-instantiated whenever a player opens an inventory.
 * <br>
 * All sub-classes should add slots in their constructor.
 *
 */
public class Container {

	protected TileEntity tileEntity;
	
	public Array<Slot> slots = new Array<>();
	
	/**
	 * THE TILE ENTITY MUST IMPLEMENT IINVENTORY
	 * @param player The player's inventory
	 * @param te TileEntity that implements IInventory
	 */
	public Container(InventoryPlayer player, TileEntity te){
		if(!(te instanceof IInventory)){
			throw new IllegalArgumentException("Tile entity passed in container constructor must implement IInventory");
		}
		tileEntity = te;
	}
	
}
