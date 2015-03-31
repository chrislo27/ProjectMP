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
	}

	@Override
	public void render(WorldRenderer renderer) {
		world.batch.draw(world.main.manager.get(AssetMap.get("airwhoosh"), Texture.class), visualX
				* World.tilesizex - renderer.camera.camerax,
				Main.convertY(visualY * World.tilesizey - renderer.camera.cameray) - 32);
	}

}
