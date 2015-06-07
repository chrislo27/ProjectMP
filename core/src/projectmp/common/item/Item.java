package projectmp.common.item;

import projectmp.client.animation.LoopingAnimation;
import projectmp.common.TexturedObject;


public class Item extends TexturedObject{

	String name = "unnamed";
	
	@Override
	public Item addAnimation(LoopingAnimation a){
		super.addAnimation(a);
		
		return this;
	}
	
}
