package projectmp;

import projectmp.LevelData.LevelType;
import projectmp.transition.FadeIn;
import projectmp.transition.FadeOut;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

public class BackstoryScreen extends MiscLoadingScreen {

	public BackstoryScreen(Main m) {
		super(m);
	}

	private String story = "";
	private Updateable next;
	private FileHandle level;
	private LevelType nextType = LevelType.NORMAL;
	private boolean loaded = false;

	public void render(float delta) {
		super.render(delta);

		main.batch.begin();
		main.font.setColor(1, 1, 1, 1);
		if (story != null) main.font.drawWrapped(main.batch,
				Translator.getMsg("backstory." + story), Settings.DEFAULT_WIDTH / 4f,
				Main.convertY(Gdx.graphics.getHeight() * 0.4f), Settings.DEFAULT_WIDTH / 2f);

		main.font.setColor(1, 1, 1, 1);
		main.drawCentered(Translator.getMsg("conversation.next"), Settings.DEFAULT_WIDTH / 2,
				Main.convertY(Gdx.graphics.getHeight() * 0.8f));
		main.font.setColor(1, 1, 1, 1);
		main.batch.setColor(Color.BLACK);
		main.fillRect(Settings.DEFAULT_WIDTH - 192, 0, 192, 128);
		main.batch.setColor(Color.WHITE);

		main.batch.end();

		if (!loaded) {
			Main.GAME.world.load(level);
			Main.GAME.world.levelType = nextType;
			
			loaded = true;
			if(story != null){
				if(Translator.getMsg("backstory." + story).equals("")){
					main.transition(null, new FadeOut(), next);
				}
			}else{
				main.transition(null, new FadeOut(), next);
			}
		}
	}

	public void prepare(String st, FileHandle level, LevelType t) {
		story = st;
		next = Main.GAME;
		this.level = level;
		loaded = false;
		nextType = t;
	}

	public void renderUpdate() {
		super.renderUpdate();
		if (loaded) {
			if (Gdx.input.isKeyJustPressed(Keys.SPACE) || Gdx.input.isKeyJustPressed(Keys.ENTER)) {
				main.transition(new FadeIn(), new FadeOut(), next);
			}
		}

	}

}
