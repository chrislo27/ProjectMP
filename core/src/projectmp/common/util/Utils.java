package projectmp.common.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import projectmp.common.Main;
import projectmp.common.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Utils {

	private Utils() {
	}
	
	private static HashMap<Integer, Boolean> pressedButtons = new HashMap<>();

	public static <T> boolean addToArray(T[] array, T toadd) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				array[i] = toadd;
				return true;
			}
		}

		return false;
	}

	public static int getUnsignedByte(byte b) {
		return (b & 0xFF);
	}

	/**
	 * WARNING: slow method!
	 * @return
	 */
	public static int findFreePort() {
		ServerSocket socket = null;
		int port = -1;
		try {
			socket = new ServerSocket(0);
			port = socket.getLocalPort();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (port == Settings.DEFAULT_PORT) return findFreePort();

		return port;
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

	/**
	 * Draws toMask texture that's masked with the stencil texture.
	 * @param main
	 * @param toMask
	 * @param stencil
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void drawMaskedTexture(Main main, Batch batch, Texture theMask, Texture baked,
			float x, float y, float width, float height) {
		Main.useMask(theMask);
		batch.draw(baked, x, y, width, height);
	}

	public static String repeat(String s, int times) {
		String r = "";
		for (int i = 0; i < times; i++) {
			r += s;
		}
		return r;
	}
	
	public static boolean isButtonJustPressed(int button){
		if(Gdx.input.isButtonPressed(button)){
			if (pressedButtons.get(button) == null || !pressedButtons.get(button)) {
				pressedButtons.put(button, true);

				return true;
			}
		}else{
			pressedButtons.put(button, false);
		}
		
		return false;
	}
}
