package projectmp.common.entity;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.util.AssetMap;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class EntitySquare extends Entity {

	public EntitySquare(World w, float posx, float posy) {
		super(w, posx, posy);
	}

	@Override
	public void prepare() {
		this.gravityCoefficient = 0;
		this.hasBlockCollision = false;
		this.hasEntityCollision = false;
	}

	@Override
	public void render(WorldRenderer renderer) {
		world.batch.draw(world.main.manager.get(AssetMap.get("money"), Texture.class), x
				* World.tilesizex - renderer.camera.camerax,
				Main.convertY(y * World.tilesizey - renderer.camera.cameray) - 32);
		world.batch.draw(world.main.manager.get(AssetMap.get("particlestar"), Texture.class), visualx
				* World.tilesizex - renderer.camera.camerax,
				Main.convertY(visualy * World.tilesizey - renderer.camera.cameray) - 32);
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();
	}

}
