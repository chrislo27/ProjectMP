package projectmp.blocks;

import projectmp.LevelEditor.EditorGroup;
import projectmp.util.MathHelper;
import projectmp.util.ParticlePool;
import projectmp.world.World;

import com.badlogic.gdx.math.MathUtils;


public class BlockCollectible extends Block{

	public BlockCollectible(String path, String col) {
		super(path);
		collectible = col;
		this.levelEditorGroup = EditorGroup.COLLECT;
	}
	
	String collectible = "COLLECTIBLE BULEAH";
	
	@Override
	public void tickUpdate(World world, int x, int y) {
		if(Block.entityIntersects(world, x, y, world.getPlayer())){
			world.global.setInt(collectible, world.global.getInt(collectible) + 1);
			world.setBlock(null, x, y);
		}
	}

	@Override
	public boolean isRenderedFront() {
		return true;
	}
	
	@Override
	public void render(World world, int x, int y){
		super.render(world, x, y);
	}
	
	public static void glowyParticles(World world, int x, int y){
		world.particles.add(ParticlePool
				.obtain()
				.setTexture("checkpoint")
				.setPosition(x + 0.5f + MathUtils.random(-0.25f, 0.25f),
						y + 0.5f + MathUtils.random(-0.25f, 0.25f)).setStartScale(0.2f)
				.setEndScale(0.1f).setLifetime(0.5f).setAlpha(0.25f)
				.setVelocity(MathUtils.random(-0.5f, 0.5f), -MathUtils.random(0.5f, 1.1f)));
	}
	
	public static float getFloatingOffset(World world, int x, int y){
		return ((World.tilesizey / 8f)
						* ((MathHelper.clampNumberFromTime(System.currentTimeMillis()
								+ (2500 - ((x % 4) * 625)), 2.5f) * 2f) - 0.5f));
	}

}
