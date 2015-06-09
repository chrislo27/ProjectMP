package projectmp.client.ui;

import projectmp.common.Main;
import projectmp.common.Translator;
import projectmp.common.util.AssetMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Button extends UiElement {

	public String text;

	/**
	 * set width and/or height to -1 to set default (160, 32)
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
	 */
	public Button(int x, int y, int width, int height, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;

		if (width == -1) this.width = 160;
		if (height == -1) this.height = 32;
	}

	public void imageRender(Main main, String img) {
		main.batch.draw(main.manager.get(AssetMap.get(img), Texture.class), x, y, width, height);
		if (UiContainer.mouseIn(this)) {
			main.batch.setColor(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, 0.42f);
			main.batch
					.draw(main.manager.get(AssetMap.get(img), Texture.class), x, y, width, height);
			main.batch.setColor(Color.WHITE);
		}
	}

	@Override
	public void render(Main main) {
		imageRender(main, "guibg");
		renderText(main, Translator.getMsg(text), this.width);
	}

	protected void renderText(Main main, String text, float width) {
		main.font.setColor(Color.BLACK);

		if (main.font.getBounds(text).width + 6 > width) {
			main.font.setScale(width / (main.font.getBounds(text).width + 6));
		}
		main.drawCentered(main.font, text, x + (width / 2),
				y + (height / 2) + (main.font.getBounds(text).height / 2));
		main.font.setScale(1f);
	}

	@Override
	public boolean onLeftClick() {
		return false;
	}

	@Override
	public boolean onRightClick() {
		return false;
	}

	@Override
	public boolean visible() {
		return true;
	}

}
