package projectmp.common.block;

import projectmp.common.energy.IPowerHandler;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.world.World;


public abstract class BlockPowerHandler extends BlockContainer{

	public BlockPowerHandler(String unlocalName) {
		super(unlocalName);
	}
	
	public IPowerHandler getPowerHandler(World world, int x, int y){
		TileEntity te = world.getTileEntity(x, y);
		if(te == null){
			world.setTileEntity(this.createNewTileEntity(x, y), x, y);
			te = world.getTileEntity(x, y);
		}
		
		return (IPowerHandler) te;
	}

}
