package projectmp.common.entity;

import projectmp.common.world.ServerWorld;
import projectmp.common.world.World;

import com.badlogic.gdx.math.MathUtils;


public abstract class EntityLiving extends Entity{

	public float jump = 15f;
	public int maxhealth = 100;
	public int health = 100;
	
	public EntityLiving(){
		super();
	}
	
	public EntityLiving(World w, float posx, float posy) {
		super(w, posx, posy);
	}
	
	public void jump() {
		if ((getBlockCollidingUp() == null && getEntityCollidingUp() == null)
				&& (getBlockCollidingDown() != null || getEntityCollidingDown() != null)) {
			veloy = -jump;
		}
	}
	
	public void damage(int dmg){
		health = MathUtils.clamp(health - dmg, 0, maxhealth);
		if(world instanceof ServerWorld && world.isServer == true){
			((ServerWorld) world).sendHealthUpdate(this);
		}
	}

}
