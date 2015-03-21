package projectmp.blocks;

import projectmp.entity.Entity;
import projectmp.entity.EntityLiving;
import projectmp.util.MathHelper;
import projectmp.world.World;

public class BlockFire extends Block {

	public BlockFire(String path) {
		super(path);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		for (Entity e : world.entities) {
			if (e instanceof EntityLiving) {
				if (MathHelper.intersects(x, y, 2, 1, e.x, e.y, e.sizex, e.sizey)) {
					((EntityLiving) e).setFire(1);
				}
			}
		}
	}

	@Override
	public boolean isRenderedFront() {
		return true;
	}

}
