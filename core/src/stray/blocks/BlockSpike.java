package stray.blocks;

import stray.entity.Entity;
import stray.entity.EntityLiving;
import stray.util.DamageSource;
import stray.util.MathHelper;
import stray.world.World;

public class BlockSpike extends Block {

	public BlockSpike(String path) {
		super(path);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		for (Entity e : world.entities) {
			if (e instanceof EntityLiving) {
				if (Block.entityIntersects(world, x + 0.2f, y + 0.75f, e, 0.6f, 0.25f)) {
					if (((EntityLiving) e).invincibility == 0) ((EntityLiving) e).damage(9001, DamageSource.spikes);
				}
			}
		}
	}
}
