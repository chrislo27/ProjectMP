package projectmp.client.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;

public class LightingUpdate implements Poolable {

	int x = 0;
	int y = 0;
	byte brightness = 0;
	int color = Color.rgb888(1, 1, 1);

	public LightingUpdate() {

	}

	@Override
	public void reset() {
	}

	public LightingUpdate set(int x, int y, byte bright, int color) {
		this.x = x;
		this.y = y;
		brightness = bright;
		this.color = color;

		return this;
	}

}
