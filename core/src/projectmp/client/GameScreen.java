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
	}

	@Override
	public void tickUpdate() {
		main.clientLogic.tickUpdate();
	}

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
