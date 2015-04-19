package projectmp.common.entity;

import com.badlogic.gdx.graphics.Texture;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.util.AssetMap;
import projectmp.common.world.World;

public class EntityPlayer extends EntityLiving {

	public String username = "UNKNOWN PLAYER NAME RAWR";

	public EntityPlayer(){
		super();
	}
	
	public EntityPlayer(World w, float posx, float posy) {
		super(w, posx, posy);
	}

	@Override
	public void prepare() {
		this.maxspeed = 15f;
		this.accspeed = maxspeed * 5f;
		this.sizex = 1f;
		this.sizey = 1f;
		this.hasEntityCollision = true;
	}

	@Override
	public void render(WorldRenderer renderer) {
		world.batch.draw(world.main.manager.get(AssetMap.get("airwhoosh"), Texture.class), renderer.convertWorldX(visualX),
				renderer.convertWorldY(visualY, World.tilesizey * sizey));
	}

}
