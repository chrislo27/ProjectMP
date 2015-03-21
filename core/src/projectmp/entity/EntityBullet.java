package projectmp.entity;

import projectmp.world.World;

public abstract class EntityBullet extends Entity {

	public EntityBullet(World world, float x, float y) {
		super(world, x, y);
	}

	boolean hasHit = false;
	boolean multiplehits = false;
	public float maxspeed = 8f;

	@Override
	public boolean isDead() {
		return ((!multiplehits && hasHit) || super.isDead());
	}

}
