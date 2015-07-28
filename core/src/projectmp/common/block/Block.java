package projectmp.common.block;

import projectmp.client.animation.Animation;
import projectmp.common.Main;
import projectmp.common.TexturedObject;
import projectmp.common.Translator;
import projectmp.common.block.droprate.DropRate;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityItem;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.item.Items;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Block extends TexturedObject {

	public static final float DEFAULT_TRANSPARENT_LIGHT = 0.05f;
	public static final float DEFAULT_OPAQUE_LIGHT = 0.2f;

	int collision = BlockFaces.NONE;
	private float lightBlocked = DEFAULT_TRANSPARENT_LIGHT;

	protected String unlocalizedName = "unnamed";

	protected float hardness = 1f;
	
	private Array<DropRate> droppedItems = new Array<>(1);

	public Block(String unlocalName) {
		super("block", unlocalName);
		unlocalizedName = unlocalName;
	}
	
	/**
	 * Called when the block is added to the list after creating the Item instance of the block
	 */
	public void initialize(){
		initializeDroppedItems();
	}
	
	/**
	 * Called in initialization to create the item stacks
	 */
	public void initializeDroppedItems(){
		droppedItems.add(new DropRate("block_" + Blocks.instance().getKey(this), 1, 1, 1));
	}

	public Array<DropRate> getDroppedItems(){
		return droppedItems;
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
	 * Triggered when the block is set in the world
	 * @param world
	 * @param x
	 * @param y
	 * @param player
	 */
	public void onPlace(World world, int x, int y) {

	}
	
	/**
	 * Triggered when the block is replaced in the world (either with air or something else)
	 * @param world
	 * @param x
	 * @param y
	 * @param player
	 */
	public void onActivate(World world, int x, int y) {

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

	public void onBreak(World world, int x, int y) {
		if (world.isServer) dropItems(world, x, y);
	}

	public void dropItems(World world, int x, int y) {
		for(int i = 0; i < droppedItems.size; i++){
			DropRate dr = droppedItems.get(i);
			if(dr.getItem() == null) continue;
			
			ItemStack stack = new ItemStack(dr.item, dr.getRandomQuantity());
			if(stack == null || stack.isNothing()) continue;
			
			world.createNewEntity(createEntityItem(world, x, y, stack));
		}
	}
	
	public EntityItem createEntityItem(World world, int x, int y, ItemStack stack){
		if(stack == null || stack.isNothing()) return null;
		
		EntityItem item = new EntityItem(world, x + 0.25f, y
				+ 0.25f, stack);
		
		item.velox += MathUtils.random(3f, 7.5f) * MathUtils.randomSign();
		item.veloy -= 4f;
		
		return item;
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

	public Block setHardness(float hard) {
		hardness = Math.max(hard, 0f);

		return this;
	}

	public float getHardness() {
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

	public void renderIndexAt(Batch batch, Main main, World world, float x, float y, float width,
			float height, int renderingIndex, int blockX, int blockY) {
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
