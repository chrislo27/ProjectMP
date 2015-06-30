package projectmp.client.lighting;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LightingRenderer {

	private Color tempColor = new Color();

	// reused to determine shadow colour
	private Color shadowColorHelper = new Color();

	// reused to determine avg colour
	private Color averageColorHelper = new Color();

	private Color gradientColor0 = new Color();
	private Color gradientColor1 = new Color();
	private Color gradientColor2 = new Color();
	private Color gradientColor3 = new Color();

	private transient LightingEngine engine;

	public LightingRenderer(LightingEngine e) {
		engine = e;
	}

	protected void render(WorldRenderer renderer, SpriteBatch batch) {
		batch.begin();

		for (int x = renderer.getCullStartX(2); x < renderer.getCullEndX(2); x++) {
			for (int y = renderer.getCullStartY(2); y < renderer.getCullEndY(2); y++) {

				if (!Settings.smoothLighting) {
					batch.setColor(setTempColorToBlock(x, y));
					Main.fillRect(batch, renderer.convertWorldX(x),
							renderer.convertWorldY(y, World.tilesizey), World.tilesizex,
							World.tilesizey);
					continue;
				}

				renderer.main.drawGradient(batch, renderer.convertWorldX(x), renderer
						.convertWorldY(y, World.tilesizey), World.tilesizex, World.tilesizey,
						gradientColor0
								.set(setAveragedColors(x, y, x - 1, y + 1, x - 1, y, x, y + 1)),
						gradientColor1
								.set(setAveragedColors(x, y, x + 1, y + 1, x + 1, y, x, y + 1)),
						gradientColor2
								.set(setAveragedColors(x, y, x + 1, y - 1, x + 1, y, x, y - 1)),
						gradientColor3
								.set(setAveragedColors(x, y, x - 1, y - 1, x - 1, y, x, y - 1)));
			}
		}

		batch.end();
		batch.setColor(1, 1, 1, 1);
	}

	/**
	 * This sets the temp colour to the block colour AND accounts for the sky lighting
	 * @param x
	 * @param y
	 * @return
	 */
	private Color setTempColorToBlock(int x, int y) {
		// set tempColor to the block color
		Color.rgb888ToColor(tempColor, engine.getLightColor(x, y));

		// lerp the base shadow with the colour of time of day based on the sky brightness
		shadowColorHelper.set(LightingEngine.SHADOW_BASE_COLOR);
		shadowColorHelper.lerp(engine.timeOfDayColor,
				(engine.getSkyLightFromTOD(engine.getSkyLight(x, y)) / 127f));

		// lerp between shadow and actual based on alpha which is based on brightness (sky and block)
		tempColor.lerp(shadowColorHelper, calcAlpha(x, y));

		return tempColor.set(tempColor.r, tempColor.g, tempColor.b, calcAlpha(x, y));
	}

	/**
	 * calculates the alpha for the shadow based on brightness and sky brightness
	 * <br>
	 * The higher of the two (sky/block) is taken.
	 * @param x
	 * @param y
	 * @return
	 */
	public float calcAlpha(int x, int y) {
		float alpha = 1f - engine.getActualLighting(x, y);
		
		// alpha is brightness inverted (ie: full brightness is 0, darkness is 1)
		
		// this is the threshold where it becomes more dark faster
		float threshold = 0.7f;
		float thresholdInverse = 1f - threshold;
		
		// simple linear formula
		float linear = alpha * threshold;
		
		// square alpha
		float square = ((float) Math.pow(alpha, 2));
		// essentially the same as adding itself multiplied by thresholdInverse
		square *= (1 + thresholdInverse);
		// subtract a bit so it doesn't become totally pitch black too early on
		square -= (thresholdInverse / 2);
		
		if (alpha >= threshold) {
			return square;
		} else {
			return linear;
		}
	}

	private Color setAveragedColors(int startx, int starty, int x1, int y1, int x2, int y2, int x3,
			int y3) {
		float r, g, b, a;
		float r1, g1, b1, a1;
		float r2, g2, b2, a2;
		float r3, g3, b3, a3;

		setTempColorToBlock(startx, starty);
		r = tempColor.r;
		g = tempColor.g;
		b = tempColor.b;
		a = tempColor.a;

		setTempColorToBlock(x1, y1);
		r1 = tempColor.r;
		g1 = tempColor.g;
		b1 = tempColor.b;
		a1 = tempColor.a;

		setTempColorToBlock(x2, y2);
		r2 = tempColor.r;
		g2 = tempColor.g;
		b2 = tempColor.b;
		a2 = tempColor.a;

		setTempColorToBlock(x3, y3);
		r3 = tempColor.r;
		g3 = tempColor.g;
		b3 = tempColor.b;
		a3 = tempColor.a;

		averageColorHelper.set((r + r1 + r2 + r3) / 4f, (g + g1 + g2 + g3) / 4f,
				(b + b1 + b2 + b3) / 4f, (a + a1 + a2 + a3) / 4f);

		return averageColorHelper;
	}

}
