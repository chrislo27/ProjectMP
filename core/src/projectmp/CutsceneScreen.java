package projectmp;

import projectmp.conversation.Conversation;
import projectmp.transition.Transition;
import projectmp.util.AssetMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class CutsceneScreen extends Updateable {

	public CutsceneScreen(Main m) {
		super(m);
	}

	Conversation conv = null;
	Updateable next = null;
	Transition from;
	Transition to;

	private int wait = 5;
	private boolean entered = false;

	/**
	 * 
	 * @param con the conversation to show
	 * @param from the transition to use to the next screen (may be null)
	 * @param to the transition to use to the next screen (may be null)
	 * @param next the next screen after this
	 */
	public void prepare(Conversation con, Transition from, Transition to, Updateable next) {
		this.from = from;
		this.to = to;
		this.next = next;
		conv = con;
	}

	public static final int CUTSCENE_WIDTH = 480;
	public static final int CUTSCENE_HEIGHT = 320;

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (main.getConv() != null) {
			main.batch.begin();
			if (main.getConv().getCurrent().imagepath != null) {
				Texture t = main.manager.get("images/cutscene/"
						+ main.getConv().getCurrent().imagepath + ".png"); // 480x320
				main.batch.draw(t, (Settings.DEFAULT_WIDTH / 2) - (t.getWidth() / 2),
						((Gdx.graphics.getHeight() + 128) / 2) - (t.getHeight() / 2), 480, 320);
				main.batch.setColor(Color.WHITE);
			}

			main.font.setColor(Color.WHITE);
			main.drawInverse(Translator.getMsg("conversation.skip"), Settings.DEFAULT_WIDTH - 5,
					Main.convertY(10));
			main.batch.end();
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && wait == 0) {
			main.setConv(null);
		}
	}

	@Override
	public void tickUpdate() {
		
		if (wait == 0 && main.getConv() != conv && !entered) {
			main.setConv(conv);
			entered = true;
		}
		if (main.getConv() == null && entered) {
			main.transition(from, to, next);
		}
		if (wait > 0) wait--;
	}

	@Override
	public void renderDebug(int starting) {

	}

	public void onConvNext() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		wait = 5;
		entered = false;
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

	@Override
	public void renderUpdate() {
	}

}
