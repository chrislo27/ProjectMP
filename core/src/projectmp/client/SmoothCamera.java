package projectmp.client;

import projectmp.common.Settings;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class SmoothCamera {

	public float camerax = 0;
	public float cameray = 0;

	float velox = 0;
	float veloy = 0;

	public float wantedx = 0;
	public float wantedy = 0;

	private transient World world;

	public float SPEED = 8f;

	private float shakeTime = 0;
	private float shakeIntensity = 1f;
	private boolean shakeFades = false;

	public SmoothCamera(World w) {
		setWorld(w);
	}

	public void setWorld(World w) {
		world = w;
	}

	/**
	 * 
	 * @param time
	 * @param intensity
	 *            float in block sizes
	 */
	public void shake(float time, float intensity, boolean fades) {
		shakeTime = time;
		shakeIntensity = intensity;
		shakeFades = fades;
	}

	public void update() {

		if (Math.abs(wantedx - camerax) > 0.005f) {
			velox = SPEED * (wantedx - camerax);
		} else {
			velox = 0;
			camerax = wantedx;
		}
		if (Math.abs(wantedy - cameray) > 0.005f) {
			veloy = SPEED * (wantedy - cameray);
		} else {
			veloy = 0;
			cameray = wantedy;
		}

		clamp();

		camerax += velox * Gdx.graphics.getDeltaTime();

		cameray += veloy * Gdx.graphics.getDeltaTime();

		clamp();

		if (shakeTime > 0) {
			shakeTime -= Gdx.graphics.getDeltaTime();
			if (shakeTime < 0) {
				shakeTime = 0;
				if (shakeFades) {
					shake(0.25f, shakeIntensity / 5f, false);
				}
			}
		}
	}

	public void centerOn(float x, float y) {
		centerX(x);
		centerY(y);
		clamp();
	}

	public void centerX(float x) {
		wantedx = x - (Settings.DEFAULT_WIDTH / 2f);
		if (shakeTime > 0) wantedx += MathUtils.random(
				-Math.round((shakeIntensity * World.tilesizex) * 10),
				Math.round(shakeIntensity * World.tilesizey * 10)) / 10f;
		clamp();
	}

	public void centerY(float y) {
		wantedy = y - (Settings.DEFAULT_HEIGHT / 2f);
		if (shakeTime > 0) wantedy += MathUtils.random(
				-Math.round((shakeIntensity * World.tilesizex) * 10),
				Math.round(shakeIntensity * World.tilesizey * 10)) / 10f;
		clamp();
	}

	public void forceCenterOn(float x, float y) {
		wantedx = x - (Settings.DEFAULT_WIDTH / 2f);
		wantedy = y - (Settings.DEFAULT_HEIGHT / 2f);
		camerax = wantedx;
		cameray = wantedy;
		velox = 0;
		veloy = 0;
		clamp();
	}

	public void clamp() {
		camerax = MathUtils.clamp(camerax, 0, world.sizex * World.tilesizex
				- Settings.DEFAULT_WIDTH);
		cameray = MathUtils.clamp(cameray, 0, world.sizey * World.tilesizey
				- Settings.DEFAULT_HEIGHT);

		wantedx = MathUtils.clamp(wantedx, 0, world.sizex * World.tilesizex
				- Settings.DEFAULT_WIDTH);
		wantedy = MathUtils.clamp(wantedy, 0, world.sizey * World.tilesizey
				- Settings.DEFAULT_HEIGHT);
	}

	public void translate(float x, float y) {
		wantedx += x;
		wantedy += y;
	}
}
