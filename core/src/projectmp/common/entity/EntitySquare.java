package projectmp.common.entity;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;

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
		world.batch.setColor(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
		world.main.fillRect(visualx * World.tilesizex - renderer.camera.camerax,
				Main.convertY(visualy * World.tilesizey - renderer.camera.cameray), 32, 32);
		world.batch.setColor(1, 1, 1, 1);
	}
	
	@Override
	public void tickUpdate(){
		super.tickUpdate();
		x = MathUtils.random(0f, world.sizex);
		y = MathUtils.random(0f, world.sizey);
	}

}
