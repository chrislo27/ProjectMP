package common.util;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import common.Main;

public class Utils {

	private Utils() {
	}

	public static <T> boolean addToArray(T[] array, T toadd) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				array[i] = toadd;
				return true;
			}
		}

		return false;
	}

//	public static float getSoundPan(float xpos, float camerax) {
//		return MathUtils
//				.clamp(((xpos - (Math.round((camerax + (Settings.DEFAULT_WIDTH / 2))
//						/ World.tilesizex))) / (((Settings.DEFAULT_WIDTH / 2f) - World.tilesizex) / World.tilesizex)),
//						-1f, 1f);
//	}

	public static void drawRotatedCentered(Batch batch, Texture tex, float x, float y, float width,
			float height, float rotation, boolean clockwise) {
		drawRotated(batch, tex, x - width / 2f, y - width / 2f, width, height, rotation, clockwise);
	}

	public static void drawRotated(Batch batch, Texture tex, float x, float y, float width,
			float height, float rotation, boolean clockwise) {
		drawRotated(batch, tex, x, y, width, height, width / 2f, height / 2f, rotation, clockwise,
				0, 0, tex.getWidth(), tex.getHeight());
	}

	public static void drawRotated(Batch batch, Texture tex, float x, float y, float width,
			float height, float centerX, float centerY, float rotation, boolean clockwise) {
		drawRotated(batch, tex, x, y, width, height, centerX, centerY, rotation, clockwise, 0, 0,
				tex.getWidth(), tex.getHeight());
	}

	public static void drawRotated(Batch batch, Texture tex, float x, float y, float width,
			float height, float centerX, float centerY, float rotation, boolean clockwise, int u,
			int v, int uwidth, int vheight) {
		batch.draw(tex, x, y, centerX, centerY, width, height, 1, 1, rotation
				* (clockwise ? -1f : 1f), u, v, uwidth, vheight, false, false);
	}

	public static int HSBtoRGBA8888(float hue, float saturation, float brightness) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float) Math.floor(hue)) * 6.0f;
			float f = h - (float) java.lang.Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h) {
			case 0:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (t * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 1:
				r = (int) (q * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 2:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (t * 255.0f + 0.5f);
				break;
			case 3:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (q * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 4:
				r = (int) (t * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 5:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (q * 255.0f + 0.5f);
				break;
			}
		}
		return (r << 24) | (g << 16) | (b << 8) | 0x000000ff;
	}

	public static String formatMs(long millis) {
		return String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
				millis % 1000);
	}

	public static final double epsilon = 0.015625;

	public static boolean compareFloat(float x, float y) {
		if (Math.abs(x - y) < epsilon) {
			return true;
		} else return false;
	}

	public static char getRandomLetter() {
		char randomChar = (char) ((int) 'A' + Math.random() * ((int) 'Z' - (int) 'A' + 1));
		return randomChar;
	}

	public static void drawTexWithShiny(Main main, Texture texture, float x, float y, float width,
			float height) {
		Color c = Main.getRainbow(-1);
		// draw original
		main.batch.draw(texture, x, y, width, height);

		main.batch.end();

		c = Main.getRainbow();

		main.maskRenderer.begin();
		main.maskRenderer.setColor(c.r, c.g, c.b, 0.5f);
		// use the mask (itemshine) and the texture to bake on
		Main.useMask(texture, main.getCurrentShine());
		// draw the bg
		main.maskRenderer.draw(texture, x, y, width, height);
		main.maskRenderer.end();

		main.batch.begin();
	}

	public static void drawTexMask(Main main, Texture texture, Texture mask, float x, float y,
			float width, float height) {
		Color c = Main.getRainbow(-1);
		// draw original
		main.batch.draw(texture, x, y, width, height);

		main.batch.end();

		c = Main.getRainbow();

		main.maskRenderer.begin();
		main.maskRenderer.setColor(c.r, c.g, c.b, 0.5f);
		// use the mask (itemshine) and the texture to bake on
		Main.useMask(texture, mask);
		// draw the bg
		main.maskRenderer.draw(texture, x, y, width, height);
		main.maskRenderer.end();

		main.batch.begin();
	}

	public static String repeat(String s, int times) {
		String r = "";
		for (int i = 0; i < times; i++) {
			r += s;
		}
		return r;
	}
}
