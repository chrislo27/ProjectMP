package projectmp.common.block;

import projectmp.client.animation.Animation;
import projectmp.common.Main;
import projectmp.common.TexturedObject;
import projectmp.common.Translator;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Block extends TexturedObject {

	public static final float DEFAULT_TRANSPARENT_LIGHT = 0.05f;
	public static final float DEFAULT_OPAQUE_LIGHT = 0.2f;

	int collision = BlockFaces.NONE;
	private float lightBlocked = DEFAULT_TRANSPARENT_LIGHT;

	protected String unlocalizedName = "unnamed";
	
	protected float hardness = 1f;

	public Block(String unlocalName) {
		super("block", unlocalName);
		unlocalizedName = unlocalName;
	}

	/**
	 * Triggered every tick on both server and client.
	 * @param world
	 * @param x
	 * @param y
	 */
	public void tickUpdate(World world, int x, int y) {

	}

	/**
	 * Triggered when the player clicks on it (right click; lc is for item use). This method is triggered on both server and client.
	 * @param world
	 * @param x
	 * @param y
	 */
	public void onActivate(World world, int x, int y, EntityPlayer player) {

	}

	/**
	 * Returns the rendering layer index. 0 is rendered first before entities, 1 is after entities and so on. Less than 0 is not rendered at all.
	 * @param world
	 * @param x
	 * @param y
	 * @return
	 */
	public int getRenderingLayer(World world, int x, int y) {
		return 0;
	}
	
	public void onBreak(World world, int x, int y){
		dropItems(world, x, y);
	}
	
	public void dropItems(World world, int x, int y){
		// FIXME hacky hack hack
		((EntityPlayer) world.getEntityByUUID(world.main.clientLogic.getPlayer().uuid)).getInventoryObject().setSlot(2, getDroppedItem());
	}
	
	public ItemStack getDroppedItem(){
		return new ItemStack("block_" + Blocks.instance().getKey(this), 1);
	}

	/**
	 * Returns a RGBA8888 packed int colour. The alpha determines the brightness.
	 * @param world
	 * @param x
	 * @param y
	 * @return
	 */
	public int getLightEmitted(World world, int x, int y) {
		return Color.rgba8888(0, 0, 0, 0);
	}
	
	public Block setHardness(float hard){
		hardness = Math.max(hard, 0f);
		
		return this;
	}
	
	public float getHardness(){
		return hardness;
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

	public void renderIndexAt(Batch batch, Main main, World world, float x, float y, float width, float height,
			int renderingIndex, int blockX, int blockY) {
		if (getAnimation(renderingIndex) != null) {
			batch.draw(getAnimation(renderingIndex).getCurrentFrame(), x, y, width, height);
		}
	}

	@Override
	public Block addAnimations(Animation... args) {
		super.addAnimations(args);

		return this;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getLocalizedName(ItemStack stack) {
		return Translator.instance().getMsg("block." + getUnlocalizedName() + ".name");
	}

	/**
	 * used to change the current block animation
	 * @param world
	 * @param x
	 * @param y
	 * @return
	 */
	public int getCurrentRenderingIndex(World world, int x, int y) {
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
