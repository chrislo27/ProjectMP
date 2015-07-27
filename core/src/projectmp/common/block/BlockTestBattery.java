package projectmp.common.block;

import com.badlogic.gdx.graphics.g2d.Batch;

import projectmp.common.Main;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.tileentity.TileEntityTestBattery;
import projectmp.common.world.World;


public class BlockTestBattery extends BlockPowerHandler{

	public BlockTestBattery(String unlocalName) {
		super(unlocalName);
	}

	@Override
	public TileEntity createNewTileEntity(int x, int y) {
		return new TileEntityTestBattery(x, y);
	}
	
	@Override
	public void renderIndexAt(Batch batch, Main main, World world, float x, float y, float width,
			float height, int renderingIndex, int blockX, int blockY) {
		super.renderIndexAt(batch, main, world, x, y, width, height, renderingIndex, blockX, blockY);
		
		TileEntity te = world.getTileEntity(blockX, blockY);
		if(te != null){
			TileEntityTestBattery bat = (TileEntityTestBattery) world.getTileEntity(blockX, blockY);
			main.font.draw(batch, "E: " + bat.getEnergyStored(), x, y);
		}
	}

}
