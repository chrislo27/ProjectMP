package projectmp.common.tileentity;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.energy.EnergyChain;
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
		if(world.time.totalTicks % Main.TICKS != 0) return;
		
		EnergyChain.doSpreadPower(16, x, y, world);
	}
	
}
