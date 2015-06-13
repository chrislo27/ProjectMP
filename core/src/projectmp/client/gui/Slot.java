package projectmp.client.gui;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.util.AssetMap;
import projectmp.common.util.MathHelper;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Slot {

	public static class SlotState {

		public static final int NONE = 0b0;
		public static final int MOUSE_OVER = 0b10;
		public static final int SELECTED = 0b100;

		private SlotState() {
		}
	}

	int posx = 0;
	int posy = 0;
	int width = World.tilesizex;
	int height = World.tilesizey;
	int slotNum = -1;

	public Slot(int slotNumber, int x, int y) {
		slotNum = slotNumber;
		posx = x;
		posy = y;
	}

	public void render(WorldRenderer renderer, int slotState) {
		SpriteBatch batch = renderer.batch;

		batch.setColor(1, 1, 1, 1);
		// make outline blue if selected
		if ((slotState & SlotState.SELECTED) == SlotState.SELECTED) {
			float coefficient = MathHelper.clampNumberFromTime(System.currentTimeMillis(), 1f) + 0.5f;
			batch.setColor(coefficient * (50f / 255f), coefficient * (133f / 255f), coefficient
					* (217f / 255f), 1f);
		}
		batch.draw(renderer.main.manager.get(AssetMap.get("invslot"), Texture.class), posx,
				posy, width, height);
		batch.setColor(1, 1, 1, 1);

		// lighten if mouse is over, this should be LAST
		if ((slotState & SlotState.MOUSE_OVER) == SlotState.MOUSE_OVER) {
			batch.setColor(1, 1, 1, 0.25f);
			Main.fillRect(batch, posx, posy, width, height);
			batch.setColor(1, 1, 1, 1);
		}
	}

	public boolean isMouseOver() {
		if (Main.getInputX() >= posx && Main.getInputX() <= (posx) + (width)) {
			if (Settings.DEFAULT_HEIGHT - Main.getInputY() >= posy
					&& Settings.DEFAULT_HEIGHT - Main.getInputY() <= (posy) + (height)) {
				return true;
			}
		}

		return false;
	}

}
