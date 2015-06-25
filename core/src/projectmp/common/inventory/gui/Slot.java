package projectmp.common.inventory.gui;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.inventory.Inventory;
import projectmp.common.tileentity.HasInventory;
import projectmp.common.util.AssetMap;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Slot {

	public static class SlotState {

		public static final int NONE = 0b0;
		public static final int MOUSE_OVER = 0b10;
		public static final int LEFT_MOUSE_BUTTON_CLICKED = 0b100;

		private SlotState() {
		}
	}

	int posx = 0;
	int posy = 0;
	int width = World.tilesizex;
	int height = World.tilesizey;
	int slotNum = -1;
	Inventory inventory;
	
	/**
	 * 
	 * @param inventory the inventory it corresponds to
	 * @param slotNumber the slot in the inventory (the array index)
	 * @param x
	 * @param y y origin is bottom left
	 */
	public Slot(Inventory inventory, int slotNumber, int x, int y) {
		this.inventory = inventory;
		slotNum = slotNumber;
		posx = x;
		posy = y;
	}

	public void render(WorldRenderer renderer, int slotState) {
		SpriteBatch batch = renderer.batch;

		batch.setColor(1, 1, 1, 1);
		batch.draw(renderer.main.manager.get(AssetMap.get("invslot"), Texture.class), posx,
				posy, width, height);
		batch.setColor(1, 1, 1, 1);

		// lighten if mouse button is down
		if ((slotState & SlotState.LEFT_MOUSE_BUTTON_CLICKED) == SlotState.LEFT_MOUSE_BUTTON_CLICKED) {
			batch.setColor(1, 1, 1, 0.25f);
			Main.fillRect(batch, posx, posy, width, height);
			batch.setColor(1, 1, 1, 1);
		}
		
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
