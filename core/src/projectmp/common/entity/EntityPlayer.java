package projectmp.common.entity;

import com.badlogic.gdx.graphics.Texture;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.util.AssetMap;
import projectmp.common.world.World;


public class EntityPlayer extends Entity{

	public String username = "UNKNOWN PLAYER NAME RAWR";
	
	public EntityPlayer(World w, float posx, float posy) {
		super(w, posx, posy);
	}

	@Override
	public void prepare() {
		this.maxspeed = 5f;
		this.accspeed = maxspeed * 5f;
		this.sizex = 0.5f;
		this.sizey = 0.5f;
		this.hasEntityCollision = true;
	}

	@Override
	public void render(WorldRenderer renderer) {
		if(!world.isServer){
			if (Main.GAME.getPlayer() == this) {
				world.batch.draw(world.main.manager.get(AssetMap.get("airwhoosh"), Texture.class),
						x * World.tilesizex - renderer.camera.camerax,
						Main.convertY(y * World.tilesizey - renderer.camera.cameray) + 32);
			}
		}else{
			world.batch.draw(world.main.manager.get(AssetMap.get("airwhoosh"), Texture.class), visualX
					* World.tilesizex - renderer.camera.camerax,
					Main.convertY(visualY * World.tilesizey - renderer.camera.cameray) + 32);
		}
	}

}
