package projectmp.common.inventory.gui;

import projectmp.client.ClientLogic;
import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.ItemStack;
import projectmp.common.inventory.gui.Slot.SlotState;
import projectmp.common.packet.PacketSwapSlot;
import projectmp.common.util.Utils;
import projectmp.common.util.sidedictation.Side;
import projectmp.common.util.sidedictation.SideOnly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * A Gui is the graphical interface for a client. 
 * 
 *
 */
@SideOnly(Side.CLIENT)
public abstract class Gui {

	Array<Slot> slots = new Array<Slot>();
	protected InventoryPlayer playerInv;

	protected String inventoryId;
	protected int inventoryX;
	protected int inventoryY;

	public Gui(InventoryPlayer player, String id, int invx, int invy) {
		playerInv = player;
		inventoryId = id;
		inventoryX = invx;
		inventoryY = invy;
	}

	public void render(WorldRenderer renderer, ClientLogic logic) {
		handleInput(renderer);

		SpriteBatch batch = renderer.batch;

		renderDarkBackground(batch);

		for (int i = 0; i < slots.size; i++) {
			Slot slot = slots.get(i);
			int slotState = calculateSlotState(slot);

			slot.render(renderer, slotState);
		}

		logic.main.font.draw(batch,
				"mouseStack: " + logic.mouseStack.getItemString() + " x" + logic.mouseStack.getAmount(),
				Main.getInputX(), Main.convertY(Main.getInputY() + 32));
	}

	protected int calculateSlotState(Slot slot) {
		int state = SlotState.NONE;

		if (slot.isMouseOver()) {
			state |= SlotState.MOUSE_OVER;
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				state |= SlotState.LEFT_MOUSE_BUTTON_CLICKED;
			}
		}

		return state;
	}

	public void handleInput(WorldRenderer renderer) {
		if (Utils.isButtonJustPressed(Buttons.LEFT)) {
			for (int i = 0; i < slots.size; i++) {
				Slot slot = slots.get(i);

				if (slot.isMouseOver()) {
					ItemStack tempMouse = renderer.logic.mouseStack.copy();
					int slotNumber = slot.slotNum;

					PacketSwapSlot packet = renderer.logic.getSwapSlotPacket();

					packet.mouseStack = renderer.logic.mouseStack;
					packet.slotToSwap = slotNumber;
					// things to identify the inventory
					packet.invId = inventoryId;
					packet.invX = inventoryX;
					packet.invY = inventoryY;

					renderer.logic.client.sendTCP(packet);

					break;
				}
			}
		}
	}

	public void renderDarkBackground(SpriteBatch batch) {
		float oldColor = batch.getColor().toFloatBits();

		batch.setColor(0, 0, 0, 0.25f);
		Main.fillRect(batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(oldColor);
	}

	public void onGuiOpen(WorldRenderer renderer, ClientLogic logic) {

	}

	public void onGuiClose(WorldRenderer renderer, ClientLogic logic) {

	}

	public void addPlayerInventory() {
		for (int i = 0; i < 9; i++) {
			slots.add(new Slot(this.playerInv, i, 64 + (i * 32), Settings.DEFAULT_HEIGHT - 64));
		}
	}

}
