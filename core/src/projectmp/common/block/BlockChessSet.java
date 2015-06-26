package projectmp.common.block;

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
			world.main.clientLogic.setCurrentGui(GuiRegistry.instance().createNewGuiObject(
					"chessboard", world, (InventoryPlayer) player.getInventoryObject(), x, y));
		}
	}

}
