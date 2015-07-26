package projectmp.common.tileentity;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

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
			inv = new InventoryChessboard(x, y);
		}
		
		return inv;
	}

	@Override
	public void setInventoryObject(Inventory inv) {
		this.inv = (InventoryChessboard) inv;
	}

	@Override
	public void writeToNBT(TagCompound tag) {
		TagCompound invTag = new TagCompound("Inventory");
		inv.writeToNBT(invTag);
		tag.setTag(invTag);
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		TagCompound invTag = null;
		try{
			invTag = tag.getCompound("Inventory");
		}catch(TagNotFoundException e){
			inv = null;
			invTag = null;
			return;
		}
		
		if(invTag != null){
			getInventoryObject().readFromNBT(invTag);;
		}
	}
	
}
