package stray.blocks;

import com.badlogic.gdx.math.MathUtils;

import stray.entity.Entity;
import stray.util.ParticlePool;
import stray.world.World;

public class BlockAirVent extends BlockDirectional {

	public BlockAirVent(String path) {
		super(path, true);
	}

	public static final int range = 3;
	public static final float particleVelo = 15f;

	@Override
	public void tickUpdate(World world, int x, int y) {
		float newRange = range;
		if (world.getMeta(x, y) <= BlockFaces.UP) {
			for (int i = 0; i < range; i++) {
				if ((world.getBlock(x, y - i - 1).isSolid(world, x, y - i - 1) & BlockFaces.DOWN) == BlockFaces.DOWN) {
					newRange = i;
					break;
				}
			}

			if (newRange <= 0) return;

			for (Entity e : world.entities) {
				if (Block.entityIntersects(world, x - World.tilepartx, y - newRange, e,
						1 + (world.tilepartx * 2), newRange)) {
					e.veloy -= 1f;
				}
			}

			if (world.tickTime % 2 == 0) return;

			world.particles.add(ParticlePool.obtain().setTexture("airwhoosh")
					.setPosition(x + 0.5f + MathUtils.random(-0.25f, 0.25f), y)
					.setVelocity(MathUtils.random(-0.075f, 0.075f), -(particleVelo))
					.setLifetime(newRange / particleVelo).setStartScale(2f).setEndScale(2f)
					.setAlpha(0.5f).setDestroyOnBlock(true).setInitRotation(0));
		} else if ((world.getMeta(x, y) & BlockFaces.DOWN) == BlockFaces.DOWN) {
			for (int i = 0; i < range; i++) {
				if ((world.getBlock(x, y + i + 1).isSolid(world, x, y + i + 1) & BlockFaces.UP) == BlockFaces.UP) {
					newRange = i;
					break;
				}
			}

			if (newRange <= 0) return;

			for (Entity e : world.entities) {
				if (Block.entityIntersects(world, x - World.tilepartx, y + 1, e,
						1 + (world.tilepartx * 2), newRange)) {
					e.veloy += 1f;
				}
			}

			if (world.tickTime % 2 == 0) return;

			world.particles.add(ParticlePool.obtain().setTexture("airwhoosh")
					.setPosition(x + 0.5f + MathUtils.random(-0.25f, 0.25f), y + 1)
					.setVelocity(MathUtils.random(-0.075f, 0.075f), (particleVelo))
					.setLifetime(newRange / particleVelo).setStartScale(2f).setEndScale(2f)
					.setAlpha(0.5f).setDestroyOnBlock(true).setInitRotation(180));
		} else if ((world.getMeta(x, y) & BlockFaces.LEFT) == BlockFaces.LEFT) {
			for (int i = 0; i < range; i++) {
				if ((world.getBlock(x - i - 1, y).isSolid(world, x - i - 1, y) & BlockFaces.RIGHT) == BlockFaces.RIGHT) {
					newRange = i;
					break;
				}
			}

			if (newRange <= 0) return;

			for (Entity e : world.entities) {
				if (Block.entityIntersects(world, x - newRange, y - World.tileparty, e, newRange,
						1 + World.tileparty)) {
					e.velox -= 1f;
				}
			}

			if (world.tickTime % 2 == 0) return;

			world.particles.add(ParticlePool.obtain().setTexture("airwhoosh")
					.setPosition(x, y + 0.5f + MathUtils.random(-0.25f, 0.25f))
					.setVelocity(-(particleVelo), MathUtils.random(-0.075f, 0.075f))
					.setLifetime(newRange / particleVelo).setStartScale(2f).setEndScale(2f)
					.setAlpha(0.5f).setDestroyOnBlock(true).setInitRotation(270));
		} else if ((world.getMeta(x, y) & BlockFaces.RIGHT) == BlockFaces.RIGHT) {
			for (int i = 0; i < range; i++) {
				if ((world.getBlock(x + i + 1, y).isSolid(world, x + i + 1, y) & BlockFaces.LEFT) == BlockFaces.LEFT) {
					newRange = i;
					break;
				}
			}

			if (newRange <= 0) return;

			for (Entity e : world.entities) {
				if (Block.entityIntersects(world, x + 1, y - World.tileparty, e, newRange,
						1 + World.tileparty)) {
					e.velox += 1f;
				}
			}

			if (world.tickTime % 2 == 0) return;

			world.particles.add(ParticlePool.obtain().setTexture("airwhoosh")
					.setPosition(x + 1, y + 0.5f + MathUtils.random(-0.25f, 0.25f))
					.setVelocity((particleVelo), MathUtils.random(-0.075f, 0.075f))
					.setLifetime(newRange / particleVelo).setStartScale(2f).setEndScale(2f)
					.setInitRotation(90));
		}
	}

	@Override
	public void render(World world, int x, int y) {
		this.renderWithOffset(world, x, y, MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
	}

}
