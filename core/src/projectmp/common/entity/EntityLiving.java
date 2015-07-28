package projectmp.common.entity;

import projectmp.common.util.NBTUtils;
import projectmp.common.util.Particle;
import projectmp.common.util.ParticlePool;
import projectmp.common.world.ServerWorld;
import projectmp.common.world.World;

import com.badlogic.gdx.math.MathUtils;
import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;

public abstract class EntityLiving extends Entity {

	public float jump = 15f;
	public int maxhealth = 100;
	public int health = 100;

	public EntityLiving() {
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

	public void damage(int oldDmg) {
		int newDamage = oldDmg;
		
		health = MathUtils.clamp(health - newDamage, 0, maxhealth);
		if (world instanceof ServerWorld && world.isServer == true) {
			((ServerWorld) world).sendHealthUpdate(this);
		} else if (world.isServer == false) {
			Particle p = ParticlePool
					.obtain()
					.setGravity(0f)
					.setVelocity(this.velox + (MathUtils.random(0.1f) * MathUtils.randomSign()),
							this.veloy + -2f);

			p.setTexture("_" + newDamage);
			p.setTint((Math.signum(newDamage) >= 0 ? 1 : 0), (Math.signum(newDamage) < 0 ? 1 : 0), 0, 1f);
			p.setPosition(x + sizex / 2
					+ (sizex * MathUtils.randomSign() * MathUtils.random(0.25f)), y - sizey / 10);
			p.setLifetime(1);
			world.particles.add(p);
		}
	}

	@Override
	public void writeToNBT(TagCompound tag) {
		super.writeToNBT(tag);

		tag.setTag(new TagInteger("MaxHealth", maxhealth));
		tag.setTag(new TagInteger("Health", health));
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		super.readFromNBT(tag);

		maxhealth = NBTUtils.getIntWithDef(tag, "MaxHealth", 100);
		health = NBTUtils.getIntWithDef(tag, "Health", maxhealth);
	}

}
