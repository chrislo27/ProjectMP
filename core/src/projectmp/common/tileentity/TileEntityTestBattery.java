package projectmp.common.tileentity;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;


public class TileEntityTestBattery extends TileEntityPowerHandler{

	public TileEntityTestBattery(int x, int y){
		super(x, y);
		this.setMaxCapacity(256);
	}
	
	public TileEntityTestBattery(){
		super();
	}
	
}
