package projectmp.common.item;

import projectmp.client.animation.LoopingAnimation;
import projectmp.common.TexturedObject;
import projectmp.common.Translator;

public class Item extends TexturedObject {

	String name = "unnamed";

	public Item(String unlocalizedname) {
		name = unlocalizedname;
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
