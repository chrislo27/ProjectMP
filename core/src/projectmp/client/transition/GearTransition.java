package projectmp.client.transition;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Utils;
import projectmp.common.util.render.StencilMaskUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GearTransition implements Transition {

	public static final float GEAR_MIDDLE_DIAMETER = 178f;
	public static final float FULL_TO_MIDDLE_RATIO = 512f / 178f;
	public static final float MIDDLE_TO_FULL_RATIO = 178f / 512f;

	float gearscale = 0f;
	float seconds = 1;

	Texture nextScreen = null;

	boolean forcefinish = false;

	public GearTransition(float seconds) {
		this.seconds = seconds;
	}

	@Override
	public boolean finished() {
		return gearscale >= 1 || forcefinish;
	}

	@Override
	public void render(Main main) {
			if (Main.TRANSITION.nextScreen == null) {
				forcefinish = true;
				return;
			}
			main.batch.end();
			main.buffer2.begin();
			Main.TRANSITION.nextScreen.render(Gdx.graphics.getDeltaTime());
			main.buffer2.end();
			nextScreen = main.buffer2.getColorBufferTexture();
			main.batch.begin();
		

		if (nextScreen == null) return;

		Texture gear = main.textures.get("gear");
		float size = (gearscale * (Settings.DEFAULT_WIDTH / GEAR_MIDDLE_DIAMETER) * Gdx.graphics
				.getWidth() / 2);

		main.batch.setColor(1, 1, 1, 1);
		Utils.drawRotated(main.batch, gear, (Settings.DEFAULT_WIDTH / 2) - (size / 2f),
				(Gdx.graphics.getHeight() / 2) - (size / 2f), size, size, 360 * MathHelper.getNumberFromTime(2f), true);

		main.batch.end();
		StencilMaskUtil.prepareMask();

		main.shapes.begin(ShapeType.Filled);
		float radius = (((GEAR_MIDDLE_DIAMETER / gear.getWidth()) * gearscale) * (Settings.DEFAULT_WIDTH / GEAR_MIDDLE_DIAMETER) * Gdx.graphics
				.getWidth()) / 2 / 2;
		main.shapes
				.circle(Settings.DEFAULT_WIDTH / 2,
						Gdx.graphics.getHeight() / 2,
						radius, 100);
		main.shapes.end();
		
		main.batch.begin();
		StencilMaskUtil.useMask();
		main.batch.draw(nextScreen, 0, Gdx.graphics.getHeight(), nextScreen.getWidth(),
				-nextScreen.getHeight());

		gearscale += Gdx.graphics.getDeltaTime() / seconds;
		main.batch.flush();
		StencilMaskUtil.resetMask();
	}

	@Override
	public void tickUpdate(Main main) {
	}

}
