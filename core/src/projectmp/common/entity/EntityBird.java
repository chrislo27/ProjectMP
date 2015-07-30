package projectmp.common.entity;

import projectmp.client.WorldRenderer;
import projectmp.common.ai.AIBird;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.world.World;


public class EntityBird extends EntityLiving{

	public EntityBird(){
		super();
		prepare();
	}
	
	public EntityBird(World world, float x, float y){
		super(world, x, y);
		prepare();
	}
	
	private void prepare(){
		this.maxhealth = 5;
		this.health = maxhealth;
		
		this.sizex = 0.5f;
		this.sizey = 0.5f;
		
		this.gravityCoefficient = 0.1f;
		
		this.maxspeed = 7.5f;
		this.accspeed = maxspeed * 4;
		
		if(world != null && world.isServer){
			this.ai = new AIBird(this);
		}
	}
	
	@Override
	public void render(WorldRenderer renderer) {
		this.drawTextureCenteredWithFacing(renderer, AssetRegistry.getTexture("bird"));
	}

}
