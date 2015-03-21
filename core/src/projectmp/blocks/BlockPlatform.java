package projectmp.blocks;

import projectmp.Main;
import projectmp.util.MathHelper;
import projectmp.util.Utils;
import projectmp.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class BlockPlatform extends BlockDirectional {

	public BlockPlatform(String path) {
		super(path, false);
	}

	@Override
	public int isSolid(World world, int x, int y) {
		return MathUtils.clamp(world.getMeta(x, y), BlockFaces.NONE, BlockFaces.ALL);
	}

	

}
