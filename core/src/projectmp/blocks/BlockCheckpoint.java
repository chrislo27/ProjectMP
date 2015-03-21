package projectmp.blocks;

import projectmp.entity.Entity;
import projectmp.entity.EntityLiving;
import projectmp.util.DamageSource;
import projectmp.util.MathHelper;
import projectmp.util.ParticlePool;
import projectmp.world.World;

import com.badlogic.gdx.math.MathUtils;

public class BlockCheckpoint extends Block {

	public BlockCheckpoint(String path) {
		super(path);
	}

	@Override
	public boolean isRenderedFront() {
		return true;
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (Block.entityIntersects(world, x, y + 0.25f, world.getPlayer(), 1, 0.75f)) {
			world.setBlock(Blocks.instance().getBlock("checkpointclaimed"), x, y);
			world.setCheckpoint(x, y);
			particles(world, x, y);
		}
	}

	private void particles(World world, int x, int y) {
		for (int i = 0; i < 25; i++) {
			world.particles.add(ParticlePool
					.obtain()
					.setTexture("checkpoint")
					.setRotation(5f, MathUtils.randomBoolean())
					.setLifetime(1.75f + MathUtils.random(1.25f))
					.setPosition(x + MathUtils.random(0.2f, 0.8f), y + MathUtils.random(0.2f, 0.8f))
					.setVelocity(1.25f * MathUtils.randomSign() * MathUtils.random(0.05f, 1f),
							-1f * MathUtils.random(0.8f, 1.2f)).setTint(1, 1, 1, 1).setAlpha(0.85f).setStartScale(0.75f).setEndScale(0.5f));
		}
	}

}
