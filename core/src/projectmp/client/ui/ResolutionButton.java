package projectmp.client.ui;

import projectmp.common.Main;
import projectmp.common.Translator;

import com.badlogic.gdx.graphics.Color;

/**
 * same as ChoiceButton but it does not use Translator and does not have a starting message
 * 
 *
 */
public class ResolutionButton extends ChoiceButton {

	public ResolutionButton(int x, int y, int w, int h, String[] choices) {
		super(x, y, w, h, "", choices);
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guibg");
		main.font.setColor(Color.BLACK);
		renderText(main, "< " + choices.get(selection) + " >", width);
	}

}
