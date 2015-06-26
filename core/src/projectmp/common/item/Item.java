package projectmp.common.item;

import projectmp.client.WorldRenderer;
import projectmp.client.animation.LoopingAnimation;
import projectmp.common.TexturedObject;
import projectmp.common.Translator;
import projectmp.common.entity.Entity;
import projectmp.common.inventory.ItemStack;
import projectmp.common.world.World;

public class Item extends TexturedObject {

	String name = "unnamed";
	int maxStackable = 512;

	public Item(String unlocalizedname) {
		name = unlocalizedname;
	}

	public void onUseStart(World world, Entity user){
		
	}
	
	public void onUsing(World world, Entity user){
		
	}
	
	public void onUseEnd(World world, Entity user){
		
	}
	
	@Override
	public Item addAnimation(LoopingAnimation a) {
		super.addAnimation(a);

		return this;
	}
	
	public Item setMaxStack(int i){
		maxStackable = i;
		return this;
	}

	public String getLocalizedName() {
		return Translator.instance().getMsg("item." + name);
	}
	
	public void render(WorldRenderer renderer, int x, int y, int width, int height, ItemStack stack){
		renderIndexAt(renderer, x, y, width, height, stack, getCurrentRenderingIndex(stack));
	}
	
	protected void renderIndexAt(WorldRenderer renderer, int x, int y, int width, int height, ItemStack stack, int renderingIndex) {
		if (getAnimation(renderingIndex) != null) {
			renderer.batch.draw(getAnimation(renderingIndex).getCurrentFrame(),
					x, y, width, height);
		}
	}
	
	protected int getCurrentRenderingIndex(ItemStack stack){
		return 0;
	}

	public int getMaxStack() {
		return maxStackable;
	}

}
