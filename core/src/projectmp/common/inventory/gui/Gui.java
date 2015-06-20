package projectmp.common.inventory.gui;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.inventory.ItemStack;
import projectmp.common.inventory.container.Container;
import projectmp.common.inventory.gui.Slot.SlotState;
import projectmp.common.util.Utils;
import projectmp.common.util.sidedictation.Side;
import projectmp.common.util.sidedictation.SideOnly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A Gui is the graphical interface for a client. It handles rendering and input only.
 * <br>
 * It essentially connects the Container object to the client's screen. As a result, it is client-only.
 * 
 *
 */
@SideOnly(Side.CLIENT)
public class Gui {

	protected Container container;
	
	protected ItemStack mouseSlot = new ItemStack(null, 0);
	
	public Gui(Container container) {
		this.container = container;
	}

	public void render(WorldRenderer renderer) {
		handleInput(renderer);

		SpriteBatch batch = renderer.batch;

		renderDarkBackground(batch);

		for(int i = 0; i < container.slots.size; i++){
			Slot slot = container.slots.get(i);
			int slotState = calculateSlotState(slot);
			
			slot.render(renderer, slotState);
		}
	}

	protected int calculateSlotState(Slot slot) {
		int state = SlotState.NONE;

		if (slot.isMouseOver()) {
			state |= SlotState.MOUSE_OVER;
			if(Gdx.input.isButtonPressed(Buttons.LEFT)){
				state |= SlotState.LEFT_MOUSE_BUTTON_CLICKED;
			}
		}

		return state;
	}

	public void handleInput(WorldRenderer renderer) {
		if (Utils.isButtonJustPressed(Buttons.LEFT)) {
			
		}
	}

	public void renderDarkBackground(SpriteBatch batch) {
		float oldColor = batch.getColor().toFloatBits();

		batch.setColor(0, 0, 0, 0.25f);
		Main.fillRect(batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(oldColor);
	}

}
