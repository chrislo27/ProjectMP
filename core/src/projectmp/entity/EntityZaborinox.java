package projectmp.entity;

import projectmp.ai.AIDumbEnemy;
import projectmp.ai.BaseAI;
import projectmp.entity.types.Enemy;
import projectmp.entity.types.Stunnable;
import projectmp.entity.types.Weighted;
import projectmp.util.AssetMap;
import projectmp.world.World;

import com.badlogic.gdx.graphics.Texture;

/**
 * basic goomba-like enemy
 * 
 *
 */
public class EntityZaborinox extends EntityLiving implements Enemy, Weighted, Stunnable {

	public EntityZaborinox(World world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public void prepare() {
		super.prepare();
		sizex = 1f - (World.tilepartx * 3);
		sizey = 1f - (World.tileparty * 3);
		this.maxspeed = 5f;
		this.accspeed = maxspeed * maxspeed;
		this.hasEntityCollision = true;
	}

	@Override
	public void renderSelf(float x, float y) {
		this.drawSpriteWithFacing(
				world.main.manager.get(AssetMap.get("entityzaborinox"), Texture.class), x, y);
	}

	@Override
	public BaseAI getNewAI() {
		return new AIDumbEnemy(this);
	}

	@Override
	public float getDamageDealt() {
		return 0;
	}

}
