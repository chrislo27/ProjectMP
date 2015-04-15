package projectmp.client;

import projectmp.client.ui.BackButton;
import projectmp.client.ui.Button;
import projectmp.client.ui.TextBox;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class DirectConnectScreen extends Updateable {

	public DirectConnectScreen(Main m) {
		super(m);

		container.elements.add(ip);
		container.elements.add(port);
		container.elements.add(connectButton);
		container.elements.add(new BackButton(Settings.DEFAULT_WIDTH - 37, Gdx.graphics
				.getHeight() - 37) {

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.MAINMENU);
				return true;
			}
		});
	}

	private TextBox ip = new TextBox((Settings.DEFAULT_WIDTH / 2) - 80, 256, 160, 32, "localhost")
			.setAllowSpecial(false).setAllowSpaces(false);
	private TextBox port = new TextBox((Settings.DEFAULT_WIDTH / 2) - 80, 192, 160, 32, ""
			+ Settings.DEFAULT_PORT).setAllowDigits(true).setAllowLetters(false)
			.setAllowSpaces(false).setAllowSpecial(false);
	private Button connectButton = new Button((Settings.DEFAULT_WIDTH / 2) - 80, 64, 160, 32, "menu.connect"){
		
		@Override
		public boolean onLeftClick(){
			Main.CONNECTING.connectTo(ip.text, Integer.parseInt(port.text));
			main.setScreen(Main.CONNECTING);
			
			return true;
		}
		
	};

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();
		main.drawInverse(main.font, Translator.getMsg("menu.player") + Main.username,
				Settings.DEFAULT_WIDTH - 5, 20);
		
		main.drawInverse(main.font, Translator.getMsg("menu.connecting.ip"),
				(Settings.DEFAULT_WIDTH / 2) - 85, 256 + 23);
		main.drawInverse(main.font, Translator.getMsg("menu.connecting.port", Settings.DEFAULT_PORT),
				(Settings.DEFAULT_WIDTH / 2) - 85, 192 + 23);
		
		container.render(main);
		main.batch.end();
	}

	@Override
	public void renderUpdate() {
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
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
