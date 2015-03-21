package stray.ui;

import stray.Main;
import stray.Translator;

import com.badlogic.gdx.graphics.Color;


public class BooleanButton extends Button{

	public BooleanButton(int x, int y, int w, int h, String text) {
		super(x, y, w, h, text);
	}
	
	public boolean state = false;
	
	@Override
	public void render(Main main) {
		imageRender(main, "guibg" + state + "");
		main.font.setColor(Color.BLACK);
//		main.drawCentered(Translator.getMsg(text), x + (width / 2) - 27,
//				y + (height / 2) + (main.font.getBounds(text).height / 2));
		renderText(main, Translator.getMsg(text), this.width - 27);
	}
	
	@Override
	public boolean onLeftClick() {
		state = !state;
		return true;
	}

	@Override
	public boolean onRightClick() {
		state = !state;
		return true;
	}
	
	public BooleanButton setState(boolean b){
		state = b;
		return this;
	}

}
