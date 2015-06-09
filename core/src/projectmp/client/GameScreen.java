package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class GameScreen extends Updateable {

	public GameScreen(Main m) {
		super(m);
	}

	@Override
	public void render(float delta) {
		main.clientLogic.render();
	}

	@Override
	public void renderUpdate() {
		main.clientLogic.renderUpdate();

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			greenx = ((int) ((Main.getInputX() + main.clientLogic.renderer.camera.camerax) / World.tilesizex));
			greeny = ((int) ((Main.getInputY() + main.clientLogic.renderer.camera.cameray) / World.tilesizey));

			main.clientLogic.world.lightingEngine.setLightSource((byte) 127, Color.rgb888(1, 0, 0),
					redx, redy);
			main.clientLogic.world.lightingEngine.setLightSource((byte) 127, Color.rgb888(0, 1, 0),
					greenx, greeny);

			main.serverLogic.world.setBlock(null, greenx, greeny);
		}
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			redx = ((int) ((Main.getInputX() + main.clientLogic.renderer.camera.camerax) / World.tilesizex));
			redy = ((int) ((Main.getInputY() + main.clientLogic.renderer.camera.cameray) / World.tilesizey));

			main.clientLogic.world.lightingEngine.setLightSource((byte) 127, Color.rgb888(1, 0, 0),
					redx, redy);
			main.clientLogic.world.lightingEngine.setLightSource((byte) 127, Color.rgb888(0, 1, 0),
					greenx, greeny);
		}
	}

	@Override
	public void tickUpdate() {
		main.clientLogic.tickUpdate();
	}

	private int greenx = 0;
	private int greeny = 0;
	private int redx = 0;
	private int redy = 0;

	@Override
	public void renderDebug(int starting) {
		main.clientLogic.renderDebug(starting);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
