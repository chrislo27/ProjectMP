package projectmp.client.ui;

import projectmp.common.Main;
import projectmp.common.Translator;
import projectmp.common.util.AssetMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class LanguageButton extends Button {

	public LanguageButton(int x, int y) {
		super(x, y, 48, 48, null);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guilanguage");
		main.font.setColor(Color.WHITE);
		main.font.draw(main.batch,
				Translator.getMsg("menu.language") + ": "
						+ Translator.instance().languageList.get(Translator.instance().toUse),
				x + width + 5, y + (height / 2));
	}

	@Override
	public boolean onLeftClick() {
		Translator.instance().nextLang();
		Main.getPref("settings").putString("language", Translator.instance().currentLang()).flush();
		return true;
	}
	@Override
	public boolean onRightClick() {
		Translator.instance().prevLang();
		Main.getPref("settings").putString("language", Translator.instance().currentLang()).flush();
		return true;
	}
}
