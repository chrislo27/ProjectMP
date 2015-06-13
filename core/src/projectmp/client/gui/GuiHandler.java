package projectmp.client.gui;

import projectmp.client.WorldRenderer;
import projectmp.client.gui.Slot.SlotState;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.util.Utils;
import projectmp.common.util.sidedictation.Side;
import projectmp.common.util.sidedictation.SideOnly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

@SideOnly(Side.CLIENT)
public class GuiHandler {

	Array<Slot> slots = new Array<>();

	int selectedSlot = -1;

	public GuiHandler() {

	}

	public void render(WorldRenderer renderer) {
		handleInput(renderer);

		SpriteBatch batch = renderer.batch;

		renderDarkOverlay(batch);

		for (int i = 0; i < slots.size; i++) {
			Slot slot = slots.get(i);

			slot.render(renderer, calculateSlotState(slot));
		}
	}

	protected int calculateSlotState(Slot slot) {
		int state = SlotState.NONE;

		if (slot.isMouseOver()) {
			state |= SlotState.MOUSE_OVER;
		}

		if (selectedSlot >= 0) {
			if (slots.get(selectedSlot) == slot) {
				state |= SlotState.SELECTED;
			}
		}

		return state;
	}

	public void handleInput(WorldRenderer renderer) {
		if (Utils.isButtonJustPressed(Buttons.LEFT)) {
			boolean alreadySelected = selectedSlot != -1;
			
			for (int i = 0; i < slots.size; i++) {
				Slot slot = slots.get(i);

				if (slot.isMouseOver()) {
					if (selectedSlot == -1) {
						selectedSlot = i;
					} else {
						if(slot != slots.get(selectedSlot)){
							// TODO swap contents
						}
						selectedSlot = -1;
					}
					break;
				}
			}
			
			// if someone clicks outside to deselect
			if(selectedSlot != -1 && alreadySelected){
				selectedSlot = -1;
			}
		}
	}

	public void renderDarkOverlay(SpriteBatch batch) {
		float oldColor = batch.getColor().toFloatBits();

		batch.setColor(0, 0, 0, 0.25f);
		Main.fillRect(batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(oldColor);
	}

}
