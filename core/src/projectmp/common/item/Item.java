package projectmp.common.item;

import projectmp.client.animation.LoopingAnimation;
import projectmp.common.TexturedObject;
import projectmp.common.Translator;
import projectmp.common.entity.Entity;
import projectmp.common.world.World;

public class Item extends TexturedObject {

	String name = "unnamed";

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

	public String getLocalizedName() {
		return Translator.instance().getMsg("item." + name);
	}

}
