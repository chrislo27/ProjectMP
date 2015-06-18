package projectmp.common.tileentity;

/**
 * Blocks implement this
 * 
 *
 */
public interface ITileEntityProvider {

	public TileEntity createNewTileEntity(int x, int y);

}
