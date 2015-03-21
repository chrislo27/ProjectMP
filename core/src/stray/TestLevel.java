package stray;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;


public class TestLevel extends GameScreen{

	public TestLevel(Main m) {
		super(m);
	}

	@Override
	public void render(float delta){
		super.render(delta);
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			main.setScreen(Main.LEVELEDITOR);
		}
	}
}
