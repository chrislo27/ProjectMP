package projectmp.client.ui;

import projectmp.common.Main;
import projectmp.common.Settings;
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
		if (Main.getInputX() >= e.getX()
				&& Main.getInputX() <= (e.getX())
						+ (e.getWidth())) {
			if (Settings.DEFAULT_HEIGHT - Main.getInputY() >= e.getY()
					&& Settings.DEFAULT_HEIGHT - Main.getInputY() <= (e.getY())
							+ (e.getHeight())) {
				return true;
			}
		}

		return false;
	}
}
