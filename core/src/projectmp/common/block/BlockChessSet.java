package projectmp.common.block;

import com.badlogic.gdx.graphics.Color;

import projectmp.common.Main;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.tileentity.TileEntityChessboard;
import projectmp.common.world.World;

public class BlockChessSet extends BlockContainer {

	@Override
	public TileEntity createNewTileEntity(int x, int y) {
		return new TileEntityChessboard(x, y);
	}

	@Override
	public void onActivate(World world, int x, int y, EntityPlayer player) {
		if (world.isServer == false) {
			world.main.clientLogic.openGui("chessboard", x, y);
		}
	}
	
	@Override
	public void tickUpdate(World world, int x, int y){
		super.tickUpdate(world, x, y);
		
		if(world.isServer == false){
			world.lightingEngine.scheduleLightingUpdate();
		}
	}
	
	@Override
	public int getLightEmitted(World world, int x, int y){
		return Color.rgba8888(Main.getRainbow().r, Main.getRainbow().g, Main.getRainbow().b, 1f);
	}

}
