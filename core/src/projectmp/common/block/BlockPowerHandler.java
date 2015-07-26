package projectmp.common.block;

import projectmp.common.energy.IPowerHandler;
import projectmp.common.tileentity.TileEntity;


public abstract class BlockPowerHandler extends BlockContainer implements IPowerHandler{

	public BlockPowerHandler(String unlocalName) {
		super(unlocalName);
	}

}
