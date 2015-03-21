package stray.blocks;

import stray.Main;
import stray.entity.Entity;
import stray.util.CoordPool;
import stray.util.Coordinate;
import stray.util.ParticlePool;
import stray.world.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class BlockTeleporter extends Block {

	public BlockTeleporter(String path) {
		super(path);
	}

	public static final int COOLDOWN = Math.round(2.5f * Main.TICKS);

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (world.getMeta(x, y) > 0) world.setMeta(world.getMeta(x, y) - 1, x, y);

		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			if (world.getMeta(x, y) <= 0) {
				attemptTeleport(world, x, y, world.getPlayer());
			}
		}

		if (world.tickTime % 8 == 0) {
			world.particles
					.add(ParticlePool
							.obtain()
							.setTexture("teleporterring")
							.setPosition(x + 0.5f, y + 0.7f)
							.setLifetime(1)
							.setVelocity(0, -0.75f)
							.setTint(((world.getMeta(x, y)) / (COOLDOWN * 1f)), 0,
									((COOLDOWN - world.getMeta(x, y)) / (COOLDOWN * 1f)), 1)
							.setAlpha(0.5f));
		}
	}

	private void attemptTeleport(World world, int startx, int starty, Entity entity) {
		Array<Coordinate> teleporters = new Array<Coordinate>();
		for (int x = 0; x < world.sizex; x++) {
			for (int y = 0; y < world.sizey; y++) {
				if (x == startx && y == starty) continue;
				if (world.getBlock(x, y) instanceof BlockTeleporter) {
					teleporters.add(CoordPool.obtain().setPosition(x, y));
				}
			}
		}

		if (teleporters.size == 0) return;

		teleporters.shuffle();

		Coordinate chosen = null;

		for (int i = 0; i < teleporters.size; i++) {
			Coordinate c = teleporters.get(i);

			if (world.getMeta(c.getX(), c.getY()) <= 0) {
				chosen = c;
				break;
			}
		}

		if (chosen == null) return;

		entity.x = chosen.getX();
		entity.y = chosen.getY();
		world.setMeta(COOLDOWN, chosen.getX(), chosen.getY());
		world.setMeta(COOLDOWN, startx, starty);
		world.particles.add(ParticlePool.obtain().setPosition(startx + 0.5f, starty + 0.5f)
				.setLifetime(0.15f).setTexture("particleflash" + MathUtils.random(3))
				.setStartScale(0.9f).setEndScale(1.1f).setTint(Color.CYAN).setAlpha(0.75f));
		world.particles.add(ParticlePool.obtain().setPosition(chosen.getX() + 0.5f, chosen.getY() + 0.5f)
				.setLifetime(0.15f).setTexture("particleflash" + MathUtils.random(3))
				.setStartScale(0.9f).setEndScale(1.1f).setTint(Color.CYAN).setAlpha(0.75f));

		CoordPool.instance().getPool().freeAll(teleporters);
	}
}
