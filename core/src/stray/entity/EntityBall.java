package stray.entity;

import stray.Main;
import stray.entity.types.Weighted;
import stray.util.AssetMap;
import stray.util.Utils;
import stray.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * basic goomba-like enemy
 * 
 *
 */
public class EntityBall extends Entity implements Weighted {

	public EntityBall(World world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public void prepare() {
		sizex = 1f - (World.tilepartx * 3);
		sizey = 1f - (World.tileparty * 3);
		this.maxspeed = 5f;
		this.accspeed = maxspeed * maxspeed;
		this.hasEntityCollision = true;
		this.forceTransfer = 1f;
		this.dragCoefficient = 0.125f;
		this.bounceCoefficient = 0.75f;
		
		circumference = MathUtils.PI * (((sizex) + (sizey)) / 2f);
	}
	
	private float circumference = MathUtils.PI;
	private float rotationManipulative = MathUtils.random(9001f);

	@Override
	public void renderSelf(float x, float y) {
		Utils.drawRotated(world.batch,
				world.main.manager.get(AssetMap.get("entityball"), Texture.class), x, y - (sizey * World.tilesizey),
				sizex * World.tilesizex, sizey * World.tilesizey, getRotationFromX(), true);
	}
	
	private float getRotationFromX(){
		return (((this.x + rotationManipulative) % circumference) / circumference) * 360f;
	}

}
