package projectmp.common.block;

import com.badlogic.gdx.graphics.g2d.Batch;

import projectmp.client.WorldRenderer;
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
	public void renderInWorld(WorldRenderer renderer, float x, float y, float width,
			float height, int blockX, int blockY) {
		super.renderInWorld(renderer, x, y, width, height, blockX, blockY);
		
		TileEntity te = renderer.world.getTileEntity(blockX, blockY);
		if(te != null){
			TileEntityTestBattery bat = (TileEntityTestBattery) renderer.world.getTileEntity(blockX, blockY);
			renderer.main.font.draw(renderer.batch, "E: " + bat.getEnergyStored(), x, y);
		}
	}

}
