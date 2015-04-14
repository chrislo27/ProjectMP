package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.client.animation.LoopingAnimation;
import projectmp.common.entity.Entity;
import projectmp.common.world.World;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Block implements Disposable {

	public static final float DEFAULT_TRANSPARENT_LIGHT = 0.05f;
	public static final float DEFAULT_OPAQUE_LIGHT = 0.2f;

	int collision = BlockFaces.NONE;
	Array<LoopingAnimation> animations = new Array<LoopingAnimation>();
	private float lightBlocked = DEFAULT_TRANSPARENT_LIGHT;

	public Block() {

	}

	public LoopingAnimation getAnimation(int index) {
		return animations.get(index);
	}

	/**
	 * ordered
	 * @param a
	 * @return
	 */
	public Block addAnimation(LoopingAnimation a) {
		animations.add(a);

		return this;
	}

	public static LoopingAnimation singleBlockTexture(String path) {
		return new LoopingAnimation(1, 1, path, false);
	}

	public void tickUpdate(World world, int x, int y) {

	}

	/**
	 * overrideable for custom settings
	 */
	public float lightSubtraction(World world, int x, int y) {
		return lightBlocked;
	}

	public Block setOpaqueToLight() {
		lightBlocked = DEFAULT_OPAQUE_LIGHT;
		return this;
	}

	public Block setTransparentToLight() {
		lightBlocked = DEFAULT_TRANSPARENT_LIGHT;
		return this;
	}

	public void render(WorldRenderer renderer, int x, int y) {
		if (getAnimation(getCurrentRenderingIndex(renderer.world, x, y)) != null) {
			renderer.batch.draw(getAnimation(getCurrentRenderingIndex(renderer.world, x, y))
					.getCurrentFrame(), renderer.convertWorldX(x), renderer.convertWorldY(y,
					World.tilesizey));
		}
	}

	protected int getCurrentRenderingIndex(World world, int x, int y) {
		return 0;
	}

	public Block solidify(int faces) {
		collision = faces;
		return this;
	}

	public int isSolid(World world, int x, int y) {
		return collision;
	}

	public float getDragCoefficient(World world, int x, int y) {
		return 1;
	}

	public void onCollideLeftFace(World world, int x, int y, Entity e) {

	}

	public void onCollideRightFace(World world, int x, int y, Entity e) {

	}

	public void onCollideUpFace(World world, int x, int y, Entity e) {

	}

	public void onCollideDownFace(World world, int x, int y, Entity e) {

	}

	public void loadAnimations(){
		for (int i = 0; i < animations.size; i++) {
			if (animations.get(i) != null) animations.get(i).load();
		}
	}
	
	@Override
	public void dispose() {
		for (int i = 0; i < animations.size; i++) {
			if (animations.get(i) != null) animations.get(i).dispose();
		}
	}

	public static class BlockFaces {

		public static final int NONE = 0x0;
		public static final int ALL = 0xF;
		public static final int UP = 0x1;
		public static final int DOWN = 0x2;
		public static final int LEFT = 0x4;
		public static final int RIGHT = 0x8;

	}

}
