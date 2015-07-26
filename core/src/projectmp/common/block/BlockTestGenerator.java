package projectmp.common.block;

import projectmp.common.tileentity.TileEntity;
import projectmp.common.tileentity.TileEntityTestGenerator;


public class BlockTestGenerator extends BlockPowerHandler{

	public BlockTestGenerator(String unlocalName) {
		super(unlocalName);
	}

	@Override
	public TileEntity createNewTileEntity(int x, int y) {
		return new TileEntityTestGenerator(x, y);
	}


}
