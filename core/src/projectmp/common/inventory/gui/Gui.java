package projectmp.common.inventory.gui;

import projectmp.client.ClientLogic;
import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.ItemStack;
import projectmp.common.inventory.gui.Slot.SlotState;
import projectmp.common.packet.PacketSwapSlot;
import projectmp.common.util.Utils;
import projectmp.common.util.sidedictation.Side;
import projectmp.common.util.sidedictation.SideOnly;
import projectmp.common.world.World;

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

	protected String unlocalizedName = null;

	public Gui(InventoryPlayer player, String id, int invx, int invy) {
		playerInv = player;
		inventoryId = id;
		inventoryX = invx;
		inventoryY = invy;
	}

	public void render(WorldRenderer renderer, ClientLogic logic) {
		SpriteBatch batch = renderer.batch;

		handleInput(renderer);

		renderDarkBackground(batch);

		if (unlocalizedName != null) {
			renderer.main.font.setColor(1, 1, 1, 1);
			renderer.main.font.draw(
					batch,
					Translator.getMsg("inventory." + unlocalizedName + ".name"),
					32,
					Main.convertY(64 - (renderer.main.font.getBounds(Translator.getMsg("inventory."
							+ unlocalizedName + ".name")).height) - 4));
		}

		for (int i = 0; i < slots.size; i++) {
			Slot slot = slots.get(i);
			int slotState = calculateSlotState(slot);

			slot.render(renderer, slotState);
		}

		if (logic.mouseStack.isNothing()) {
			for (int i = 0; i < slots.size; i++) {
				Slot slot = slots.get(i);

				if (slot.isMouseOver()) {
					if (slot.inventory.getSlot(slot.slotNum).isNothing()) continue;

					renderer.main.drawTextBg(
							renderer.main.font,
							Translator.getMsg("item."
									+ slot.inventory.getSlot(slot.slotNum).getItemString()
									+ ".name")
									+ " x" + slot.inventory.getSlot(slot.slotNum).getAmount(),
							Main.getInputX(), Main.convertY(Main.getInputY() + 48));

					break;
				}
			}
		} else {
			logic.mouseStack.getItem().render(renderer, Main.getInputX(),
					Main.convertY(Main.getInputY() + World.tilesizey * 2), World.tilesizex * 2,
					World.tilesizey * 2, logic.mouseStack);
			// draw number if > 1
			if (logic.mouseStack.getAmount() > 1) {
				float textHeight = renderer.main.font.getBounds("" + logic.mouseStack.getAmount()).height;
				renderer.main.drawInverse(renderer.main.font, "" + logic.mouseStack.getAmount(),
						Main.getInputX() + World.tilesizex * 2, Main.convertY(Main.getInputY())
								+ textHeight - World.tilesizey * 2);
			}
		}
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
					PacketSwapSlot packet = renderer.logic.getSwapSlotPacket();

					// things to identify the inventory
					packet.invId = inventoryId;
					packet.invX = inventoryX;
					packet.invY = inventoryY;

					// swap stacks
					packet.mouseStack = renderer.logic.mouseStack;
					packet.slotToSwap = slot.slotNum;

					renderer.logic.client.sendTCP(packet);

					break;
				}
			}
		} else if (Utils.isButtonJustPressed(Buttons.RIGHT)) {
			for (int i = 0; i < slots.size; i++) {
				Slot slot = slots.get(i);

				if (slot.isMouseOver()) {
					PacketSwapSlot packet = renderer.logic.getSwapSlotPacket();

					// things to identify the inventory
					packet.invId = inventoryId;
					packet.invX = inventoryX;
					packet.invY = inventoryY;

					if (renderer.logic.mouseStack.isNothing()) {
						// split stack in half, giving bigger half to mouse
						
					} else {
						// if the slot is the same or empty put ONE in if under max stack amount
						if (slot.inventory.getSlot(slot.slotNum).equalsIgnoreAmount(
								renderer.logic.mouseStack)
								|| slot.inventory.getSlot(slot.slotNum).isNothing()) {

						}else{ // else swap like left click
							packet.mouseStack = renderer.logic.mouseStack;
							packet.slotToSwap = slot.slotNum;
						}
					}

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
		Slot template = new Slot(null, -1, 0, 0);

		for (int i = 0; i < 9; i++) {
			slots.add(new Slot(this.playerInv, i, (template.width / 2) + (i * template.width)
					+ (i * 4), Settings.DEFAULT_HEIGHT - (template.height * 2)));
		}
	}

	public void setUnlocalizedName(String s) {
		unlocalizedName = s;
	}

}
