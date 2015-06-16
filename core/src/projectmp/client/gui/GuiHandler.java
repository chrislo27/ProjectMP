package projectmp.client.gui;

import projectmp.client.WorldRenderer;
import projectmp.client.gui.Slot.SlotState;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.inventory.ItemStack;
import projectmp.common.util.Utils;
import projectmp.common.util.sidedictation.Side;
import projectmp.common.util.sidedictation.SideOnly;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

@SideOnly(Side.CLIENT)
public class GuiHandler {

	Array<Slot> slots = new Array<>();
	
	protected ItemStack mouseItem = new ItemStack(null, 0);

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
		
		if(mouseItem != null){
			if(mouseItem.getItem() != null && mouseItem.getAmount() > 0){
				
			}
		}
	}

	protected int calculateSlotState(Slot slot) {
		int state = SlotState.NONE;

		if (slot.isMouseOver()) {
			state |= SlotState.MOUSE_OVER;
		}

		return state;
	}

	public void handleInput(WorldRenderer renderer) {
		if (Utils.isButtonJustPressed(Buttons.LEFT)) {
			
		}
	}

	public void renderDarkOverlay(SpriteBatch batch) {
		float oldColor = batch.getColor().toFloatBits();

		batch.setColor(0, 0, 0, 0.25f);
		Main.fillRect(batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(oldColor);
	}

}
