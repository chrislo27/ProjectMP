package stray.entity;

import stray.Main;
import stray.entity.types.Inflammable;
import stray.util.DamageSource;
import stray.world.World;

public class EntityFlame extends Entity {

	public EntityFlame(World w, float posx, float posy) {
		super(w, posx, posy);
	}

	private float lifetime = 0.9f;

	@Override
	public void prepare() {
		gravityCoefficient = -0.1f;
		dragCoefficient = 0.1f;
	}

	@Override
	public void renderSelf(float x, float y) {
		world.batch.draw(world.main.animations.get("fire").getCurrentFrame(), x, y - sizey
				* World.tilesizey, sizex * World.tilesizex, sizey * World.tilesizey);
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();

		lifetime -= (1f / Main.TICKS);
		gravityCoefficient = -0.1f * lifetime;

		for (Entity e : world.entities) {
			if (e instanceof EntityLiving) {
				if (!(e instanceof Inflammable)) {
					if (intersectingOther(e)){
						((EntityLiving) e).setFire(1.5f);
						((EntityLiving) e).heal(-EntityLiving.FIRE_DAMAGE, DamageSource.yourMother);
						lifetime = 0;
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean isDead() {
		return lifetime <= 0;
	}

}
