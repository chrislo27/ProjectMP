package stray.blocks;

import com.badlogic.gdx.math.MathUtils;

import stray.Main;
import stray.util.MathHelper;
import stray.util.ParticlePool;
import stray.world.World;

public class BlockGearCollectible extends BlockCollectible {

	public BlockGearCollectible(String path) {
		super(path, collectibleName);
	}

	public static final String collectibleName = "gears";

	@Override
	public void render(World world, int x, int y) {
		super.renderWithOffset(world, x, y, 0, getFloatingOffset(world, x, y));
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		super.tickUpdate(world, x, y);

		if(world.tickTime % 2 == 0) glowyParticles(world, x, y);
	}

}
