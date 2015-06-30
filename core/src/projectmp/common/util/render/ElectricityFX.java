package projectmp.common.util.render;

import projectmp.common.Main;
import projectmp.common.util.MathHelper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class ElectricityFX {

	private ElectricityFX() {
	}
	
	public static final float DEFAULT_THICKNESS = 3f;
	public static final float DEFAULT_OFFSET_OF_POINTS = 16f;
	public static final float DEFAULT_DISTANCE_BETWEEN_POINTS = 24f;

	/**
	 * 
	 * @param batch
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param maxOffset how much "jagged" is in a point on the line
	 * @param lineThickness thickness
	 * @param distanceBetweenPoints 
	 */
	public static void drawElectricity(SpriteBatch batch, float startX, float startY, float endX,
			float endY, float maxOffset, float lineThickness, float distanceBetweenPoints) {
		float currentX = startX;
		float currentY = startY;
		float lastX = startX;
		float lastY = startY;
		
		int segments = (int) (MathHelper.calcDistance(startX, startY, endX, endY) / distanceBetweenPoints);

		for (int i = 0; i < segments + 1; i++) {
			currentX = ((endX - startX) / segments) * i + startX;
			currentY = ((endY - startY) / segments) * i + startY;
			
			// offsetting
			currentX += maxOffset * MathUtils.random(1f) * MathUtils.randomSign();
			currentY += maxOffset * MathUtils.random(1f) * MathUtils.randomSign();
			
			drawLine(batch, lastX, lastY, currentX, currentY, lineThickness);
			
			lastX = currentX;
			lastY = currentY;
		}

		drawLine(batch, currentX, currentY, endX, endY, lineThickness);
	}

	/**
	 * gets a random colour that looks lightningy
	 * @param alpha
	 * @return
	 */
	public static float getDefaultColor(float alpha) {
		return Color.toFloatBits(MathUtils.random(14f, 54f) / 255f,
				MathUtils.random(100f, 210f) / 255f, MathUtils.random(200f, 239f) / 255f, alpha);
	}

	private static void drawLine(SpriteBatch batch, float _x1, float _y1, float _x2, float _y2,
			float thickness) {
		float length = (float) MathHelper.calcDistance(_x1, _y1, _x2, _y2);
		float dx = _x1;
		float dy = _y1;
		dx = dx - _x2;
		dy = dy - _y2;
		float angle = MathUtils.radiansToDegrees * MathUtils.atan2(dy, dx);
		angle = angle - 180;
		batch.draw(Main.filltex, _x1, _y1, 0f, thickness * 0.5f, length, thickness, 1f, 1f, angle,
				0, 0, 1, 1, false, false);
	}

}
