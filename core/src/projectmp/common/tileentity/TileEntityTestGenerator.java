package projectmp.common.tileentity;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

import projectmp.common.world.World;


public class TileEntityTestGenerator extends TileEntityPowerHandler{

	public TileEntityTestGenerator(){
		super();
	}
	
	public TileEntityTestGenerator(int x, int y){
		super(x, y);
	}
	
	@Override
	public void tickUpdate(World world){
		
	}
	
}
