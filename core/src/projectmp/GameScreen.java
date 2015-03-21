package projectmp;

import projectmp.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends Updateable {

	public GameScreen(Main m) {
		super(m);
		world = new World(m);
		inputProcessor = new GameInputProcessor(main, world);
	}
	
	GameInputProcessor inputProcessor;

	World world;

	@Override
	public void render(float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (world != null) {
			world.render();
		}
	}

	@Override
	public void tickUpdate() {
		if (world != null) {
			world.tickUpdate();
		}
	}

	@Override
	public void renderUpdate() {
		if(world != null) world.renderUpdate();
		if(this == Main.GAME){
			// pause handler
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		if (world == null) world = new World(main);
		world.show();
		InputMultiplexer p = main.getDefaultInput();
		p.addProcessor(inputProcessor);
		Gdx.input.setInputProcessor(p);
	}

	@Override
	public void hide() {
		world.hide();
		Gdx.input.setInputProcessor(main.getDefaultInput());
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		if (world != null) {
			world.dispose();
		}
	}

	@Override
	public void renderDebug(int starting) {
		if (world != null) {
			world.renderer.renderDebug(starting);
		}
	}

}
