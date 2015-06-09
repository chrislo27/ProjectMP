package projectmp.common;

import projectmp.client.animation.LoopingAnimation;
import projectmp.common.block.Block;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Every object of this type has a LoopingAnimation on it
 * 
 * Intended to combine the image loading for items and blocks
 *
 */
public class TexturedObject implements Disposable {

	Array<LoopingAnimation> animations = new Array<LoopingAnimation>();

	public LoopingAnimation getAnimation(int index) {
		return animations.get(index);
	}

	public TexturedObject addAnimation(LoopingAnimation a) {
		animations.add(a);

		return this;
	}

	public void loadAnimations() {
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

}
