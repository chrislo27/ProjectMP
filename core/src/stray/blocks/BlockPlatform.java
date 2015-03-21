package stray.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import stray.Main;
import stray.util.MathHelper;
import stray.util.Utils;
import stray.world.World;

public class BlockPlatform extends BlockDirectional {

	public BlockPlatform(String path) {
		super(path, false);
	}

	@Override
	public int isSolid(World world, int x, int y) {
		return MathUtils.clamp(world.getMeta(x, y), BlockFaces.NONE, BlockFaces.ALL);
	}

	

}
