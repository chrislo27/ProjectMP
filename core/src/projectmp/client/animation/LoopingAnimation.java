package projectmp.client.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class LoopingAnimation extends OneTimeAnimation {

	public LoopingAnimation(float delay, int count, String path, boolean usesRegion) {
		super(delay, count, path, usesRegion);
		start();
	}

}
