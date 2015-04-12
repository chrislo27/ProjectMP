package projectmp.common.entity;

import projectmp.common.world.World;


public abstract class EntityLiving extends Entity{

	public float jump = 15f;
	
	public EntityLiving(World w, float posx, float posy) {
		super(w, posx, posy);
	}
	
	public void jump() {
		if ((getBlockCollidingUp() == null && getEntityCollidingUp() == null)
				&& (getBlockCollidingDown() != null || getEntityCollidingDown() != null)) {
			veloy = -jump;
		}
	}

}
