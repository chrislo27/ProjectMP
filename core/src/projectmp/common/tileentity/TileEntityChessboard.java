package projectmp.common.tileentity;

import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryChessboard;


public class TileEntityChessboard extends TileEntity implements HasInventory{

	InventoryChessboard inv;
	
	public TileEntityChessboard(){
		inv = new InventoryChessboard();
	}

	@Override
	public Inventory getInventoryObject() {
		return inv;
	}
	
}
