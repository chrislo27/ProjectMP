package projectmp.client.settingsscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import projectmp.client.ui.Slider;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;


public class AudioSettingsScreen extends SettingsScreen{

	private Slider music;
	private Slider sound;
	
	public AudioSettingsScreen(Main m) {
		super(m);
	}
	
	@Override
	protected void addGuiElements(){
		super.addGuiElements();
		
		music = new Slider((Settings.DEFAULT_WIDTH / 2) - 80,
				Settings.DEFAULT_HEIGHT - 192, 160, 32);
		sound = new Slider((Settings.DEFAULT_WIDTH / 2) - 80,
				Settings.DEFAULT_HEIGHT - 240, 160, 32);
		
		container.elements.add(music.setSlider(Settings.musicVolume));
		container.elements.add(sound.setSlider(Settings.soundVolume));
	}
	
	@Override
	public void render(float delta){
		super.render(delta);
		
		main.batch.begin();
		
		main.font.draw(main.batch,
				Translator.getMsg("menu.settings.musicvol", (int) (music.slider * 100)),
				(Settings.DEFAULT_WIDTH / 2) + 80 + (main.font.getSpaceWidth()),
				Settings.DEFAULT_HEIGHT - 192 + 20);
		main.font.draw(main.batch,
				Translator.getMsg("menu.settings.soundvol", (int) (sound.slider * 100)),
				(Settings.DEFAULT_WIDTH / 2) + 80 + (main.font.getSpaceWidth()),
				Settings.DEFAULT_HEIGHT - 240 + 20);
		
		main.font.setScale(2);
		main.font.setColor(1, 1, 1, 1);
		main.drawCentered(main.font, Translator.getMsg("menu.settings.audio"),
				(Settings.DEFAULT_WIDTH / 2), Settings.DEFAULT_HEIGHT - 32);
		main.font.setScale(1);
		
		main.batch.end();
	}

	@Override
	public void renderUpdate(){
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.setScreen(Main.SETTINGS);
		}
	}
	
	@Override
	protected void exitScreen(){
		super.exitScreen();
		main.setScreen(Main.SETTINGS);
	}
	
}
