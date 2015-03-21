package projectmp.blocks;

import projectmp.Main;
import projectmp.util.MathHelper;
import projectmp.util.ParticlePool;
import projectmp.world.World;

import com.badlogic.gdx.math.MathUtils;

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
