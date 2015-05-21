package projectmp.common.tileentity;

import com.evilco.mc.nbt.tag.TagCompound;

import projectmp.common.io.CanBeSavedToNBT;


public abstract class TileEntity implements CanBeSavedToNBT{

	protected int x = 0;
	protected int y = 0;
	
	public TileEntity(){
		
	}
	
	@Override
	public void writeToNBT(TagCompound tag) {
	}

	@Override
	public void readFromNBT(TagCompound tag) {
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public TileEntity setLocation(int x, int y){
		this.x = x;
		this.y = y;
		return this;
	}

}
