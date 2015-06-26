package projectmp.common.tileentity;

import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryChessboard;


public class TileEntityChessboard extends TileEntity implements HasInventory{

	InventoryChessboard inv;
	
	public TileEntityChessboard(){
		super();
	}
	
	public TileEntityChessboard(int x, int y){
		super(x, y);
	}

	@Override
	public Inventory getInventoryObject() {
		if(inv == null){
			inv = new InventoryChessboard("chessboard", x, y);
		}
		
		return inv;
	}
	
}
