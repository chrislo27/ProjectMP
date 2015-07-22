package projectmp.common.util.render;

import projectmp.common.Main;
import projectmp.common.util.SimplexNoise;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class LiquidContainer {

	float width, height;
	/**
	 * Gap between vertices
	 */
	float waveGap = 4;
	Color color = new Color();

	float[] waves;
	float endWave;

	SimplexNoise noise = new SimplexNoise(System.currentTimeMillis());
	float secondsElapsed = 0;

	public LiquidContainer(float w, float h, float gap, Color c) {
		width = w;
		height = h;
		waveGap = gap;
		color = c;

		waves = new float[(int) Math.max(width / waveGap, 2f)];
		for (int i = 0; i < waves.length; i++) {
			waves[i] = 0;
		}
	}

	public void render(Main main, float x, float y) {
		renderUpdate(main);

		main.verticesRenderer.begin(Main.camera.combined, GL20.GL_TRIANGLES);

		float lowestPoint = endWave;

		for (int i = 0; i < waves.length; i++) {
			if (waves[i] < lowestPoint) lowestPoint = waves[i];
		}

		// first draw a rectangle where the the liquid doesn't have spikes
		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x, y, 0);

		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width, y, 0);

		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width, y + height + lowestPoint, 0);

		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width, y + height + lowestPoint, 0);

		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x, y + height + lowestPoint, 0);

		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x, y, 0);

		// then render all our spikes with three points:
		// the vertex before it (or x, y if it's at index 0)
		// the current vertex
		// lowest point with X at the next area

		for (int i = 0; i < waves.length; i++) {
			float vertex = waves[i];
			float xPos = i * waveGap;

			// before
			main.verticesRenderer.color(color);
			if (i <= 0) {
				// starting point
				main.verticesRenderer.vertex(x, y + height + lowestPoint, 0);
			} else {
				// one before it
				main.verticesRenderer.vertex(x + xPos - waveGap, y + height + waves[i - 1], 0);
			}

			// center
			main.verticesRenderer.color(color);
			main.verticesRenderer.vertex(x + xPos, y + height + vertex, 0);

			// after
			main.verticesRenderer.color(color);
			if (i >= waves.length - 1) {
				main.verticesRenderer.vertex(x + xPos + waveGap, y + height + endWave, 0);
			} else {
				main.verticesRenderer.vertex(x + xPos + waveGap, y + height + waves[i + 1], 0);
			}

			// filling

			main.verticesRenderer.color(color);
			main.verticesRenderer.vertex(x + xPos, y + height + vertex, 0);

			main.verticesRenderer.color(color);
			main.verticesRenderer.vertex(x + xPos - waveGap + (i == 0 ? waveGap : 0), y + height
					+ lowestPoint, 0);

			main.verticesRenderer.color(color);
			main.verticesRenderer.vertex(
					x + xPos + waveGap - (i == waves.length - 1 ? waveGap : 0), y + height
							+ lowestPoint, 0);

			//if(i == waves.length - 1) continue;

			main.verticesRenderer.color(color);
			main.verticesRenderer.vertex(x + xPos, y + height + vertex, 0);

			main.verticesRenderer.color(color);
			main.verticesRenderer.vertex(x + xPos + (waveGap / 2f), y + height + lowestPoint, 0);

			main.verticesRenderer.color(color);
			main.verticesRenderer.vertex(x + xPos + waveGap, y + height
					+ (i == waves.length - 1 ? endWave : waves[i + 1]), 0);
		}

		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width, y + height + endWave, 0);
		
		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width, y + height + lowestPoint, 0);
		
		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width - waveGap / 2, y + height + lowestPoint, 0);
		
		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width - waveGap, y + height + waves[waves.length - 1], 0);
		
		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width - waveGap, y + height + lowestPoint, 0);
		
		main.verticesRenderer.color(color);
		main.verticesRenderer.vertex(x + width - waveGap / 2, y + height + lowestPoint, 0);

		main.verticesRenderer.end();
	}

	protected void renderUpdate(Main main) {
		if (Gdx.input.isKeyPressed(Keys.R)) secondsElapsed += Gdx.graphics.getDeltaTime();

		for (int i = 0; i < waves.length + 1; i++) {
			float value = (float) (noise.eval(i * 0.2f - (secondsElapsed * 2f), 64) * 32f);

			if (i == waves.length) {
				endWave = value;
			} else {
				waves[i] = value;
			}
		}
	}

}
