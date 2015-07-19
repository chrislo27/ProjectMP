package projectmp.common.item;

import projectmp.client.WorldRenderer;
import projectmp.client.animation.Animation;
import projectmp.common.TexturedObject;
import projectmp.common.Translator;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.world.World;

public class Item extends TexturedObject {

	String unlocalizedName = "unnamed";
	int maxStackable = 512;

	public Item(String unlocalizedname) {
		super("item", unlocalizedname);
		this.unlocalizedName = unlocalizedname;
	}

	public void onUseStart(World world, EntityPlayer user, int slot, int x, int y) {

	}

	public void onUsing(World world, EntityPlayer user, int slot, int x, int y) {

	}

	public void onUseEnd(World world, EntityPlayer user, int slot, int x, int y) {

	}
	
	public void onUseStartRender(WorldRenderer renderer, EntityPlayer user, int slot, int x, int y) {

	}
 
	public void onUsingRender(WorldRenderer renderer, EntityPlayer user, int slot, int x, int y) {

	}

	public void onUseEndRender(WorldRenderer renderer, EntityPlayer user, int slot, int x, int y) {

	}

	@Override
	public Item addAnimations(Animation... args) {
		super.addAnimations(args);

		return this;
	}

	public Item setMaxStack(int i) {
		maxStackable = i;
		return this;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getLocalizedName(ItemStack stack) {
		return Translator.instance().getMsg("item." + getUnlocalizedName() + ".name");
	}

	public void render(WorldRenderer renderer, float x, float y, float width, float height, ItemStack stack) {
		renderIndexAt(renderer, x, y, width, height, stack, getCurrentRenderingIndex(stack));
	}

	protected void renderIndexAt(WorldRenderer renderer, float x, float y, float width,
			float height, ItemStack stack, int renderingIndex) {
		if (getAnimation(renderingIndex) != null) {
			renderer.batch
					.draw(getAnimation(renderingIndex).getCurrentFrame(), x, y, width, height);
		}
	}

	protected int getCurrentRenderingIndex(ItemStack stack) {
		return 0;
	}

	public int getMaxStack() {
		return maxStackable;
	}

}
