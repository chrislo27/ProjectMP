package projectmp.ui;

import projectmp.Main;
import projectmp.util.AssetMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Slider implements GuiElement {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	public float slider = 0;

	private boolean grabbed = false;

	public Slider(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		if (width == -1) this.width = 160;
		if (height == -1) this.height = 32;
	}

	public Slider setSlider(float f) {
		slider = f;
		return this;
	}

	@Override
	public void render(Main main) {
		main.batch.draw(main.manager.get(AssetMap.get("guislider"), Texture.class), x, y, width,
				height);
		main.batch.draw(main.manager.get(AssetMap.get("guisliderarrow"), Texture.class), x
				+ ((width - 32) * slider), y, 32, 32);

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (Main.getInputX() >= x + ((width - 32) * slider)
					&& Main.getInputX() <= x + ((width - 32) * slider) + 32) {
				if (Main.convertY(Main.getInputY()) >= y
						&& Main.convertY(Main.getInputY()) <= y + height) {
					grabbed = true;
				}
			}
		} else {
			grabbed = false;
		}
		if (grabbed) {
			slider = MathUtils.clamp(((Main.getInputX() - x)) / (width - 32f), 0f, 1f);

		}
	}

	@Override
	public boolean visible() {
		return true;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean onLeftClick() {
		return true;
	}

	@Override
	public boolean onRightClick() {
		return false;
	}

}
