package projectmp.client.ui;

import projectmp.common.Main;
import projectmp.common.util.AssetMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Container {

	public Array<GuiElement> elements = new Array<GuiElement>();

	public void render(Main main) {
		for (GuiElement e : elements) {
			if (!e.visible()) continue;
			e.render(main);
		}
	}

	public boolean onLeftClick() {
		for (GuiElement e : elements) {
			if (!e.visible()) continue;
			if (mouseIn(e)) if (e.onLeftClick()) return true;
		}

		return false;
	}

	public boolean onRightClick() {
		for (GuiElement e : elements) {
			if (!e.visible()) continue;
			if (mouseIn(e)) if (e.onRightClick()) return true;
		}

		return false;
	}

	protected static boolean mouseIn(GuiElement e) {
		if (!e.visible()) return false;
		if (Gdx.input.getX() >= e.getX() * Main.getScaleFactorX()
				&& Gdx.input.getX() + Main.viewport.getLeftGutterWidth() <= (e.getX() * Main.getScaleFactorX())
						+ (e.getWidth() * Main.getScaleFactorX())) {
			if (Gdx.graphics.getHeight() - Gdx.input.getY() >= e.getY() * Main.getScaleFactorY()
					&& Gdx.graphics.getHeight() - Gdx.input.getY() <= (e.getY() * Main.getScaleFactorY())
							+ (e.getHeight() * Main.getScaleFactorY())) {
				return true;
			}
		}

		return false;
	}
}
