package projectmp.common.block;

import projectmp.common.tileentity.TileEntity;
import projectmp.common.tileentity.TileEntityTestBattery;


public class BlockTestBattery extends BlockPowerHandler{

	public BlockTestBattery(String unlocalName) {
		super(unlocalName);
	}

	@Override
	public TileEntity createNewTileEntity(int x, int y) {
		return new TileEntityTestBattery(x, y);
	}

}
