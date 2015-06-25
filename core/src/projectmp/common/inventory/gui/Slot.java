package projectmp.common.inventory.gui;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.ItemStack;
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
	int width = World.tilesizex * 2;
	int height = World.tilesizey * 2;
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
		// draw icon
		ItemStack stack = inventory.getSlot(slotNum);
		if(stack.getItem() != null){
			stack.getItem().render(renderer, posx, posy, width, height, stack);
			// draw number if > 1
			if(stack.getAmount() > 1){
				float textHeight = renderer.main.font.getBounds("" + stack.getAmount()).height;
				renderer.main.drawInverse(renderer.main.font, "" + stack.getAmount(), posx + width, posy + textHeight);
			}
		}
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
			
			renderer.logic.main.font.draw(batch,
					"slotStack: " + inventory.getSlot(slotNum).getItemString() + " x" + inventory.getSlot(slotNum).getAmount(),
					Main.getInputX(), Main.convertY(Main.getInputY() + 64));
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
