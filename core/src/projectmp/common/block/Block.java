package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.client.animation.LoopingAnimation;
import projectmp.common.TexturedObject;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.world.World;

public class Block extends TexturedObject {

	public static final float DEFAULT_TRANSPARENT_LIGHT = 0.05f;
	public static final float DEFAULT_OPAQUE_LIGHT = 0.2f;

	int collision = BlockFaces.NONE;
	private float lightBlocked = DEFAULT_TRANSPARENT_LIGHT;

	public Block() {

	}

	public static LoopingAnimation singleBlockTexture(String path) {
		return new LoopingAnimation(1, 1, path, false);
	}

	/**
	 * Triggered every tick.
	 * @param world
	 * @param x
	 * @param y
	 */
	public void tickUpdate(World world, int x, int y) {

	}
	
	/**
	 * Triggered when the player clicks on it (right click; lc is for item use).
	 * @param world
	 * @param x
	 * @param y
	 */
	public void onActivate(World world, int x, int y, EntityPlayer player){
		
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
		renderIndexAt(renderer, x, y, getCurrentRenderingIndex(renderer.world, x, y));
	}

	protected void renderIndexAt(WorldRenderer renderer, int x, int y, int renderingIndex) {
		if (getAnimation(renderingIndex) != null) {
			renderer.batch.draw(getAnimation(renderingIndex).getCurrentFrame(),
					renderer.convertWorldX(x), renderer.convertWorldY(y, World.tilesizey));
		}
	}

	@Override
	public Block addAnimation(LoopingAnimation a) {
		super.addAnimation(a);
		return this;
	}

	/**
	 * used to change the current block animation
	 * @param world
	 * @param x
	 * @param y
	 * @return
	 */
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

	public static class BlockFaces {

		public static final int NONE = 0x0;
		public static final int ALL = 0xF;
		public static final int UP = 0x1;
		public static final int DOWN = 0x2;
		public static final int LEFT = 0x4;
		public static final int RIGHT = 0x8;

	}

}
