package projectmp.common.inventory.gui;

import projectmp.client.ClientLogic;
import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Slot.SlotState;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.packet.PacketSwapSlot;
import projectmp.common.util.Utils;
import projectmp.common.util.annotation.sidedictation.Side;
import projectmp.common.util.annotation.sidedictation.SideOnly;
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

	public static final Slot TEMPLATE_SLOT = new Slot(null, -1, 0, 0);

	Array<Slot> slots = new Array<Slot>();
	protected InventoryPlayer playerInv;

	protected World world;

	private boolean shouldRenderPlayerInvTitle = true;

	public Gui(World world, InventoryPlayer player) {
		playerInv = player;
		this.world = world;
	}

	public void render(WorldRenderer renderer, ClientLogic logic) {
		SpriteBatch batch = renderer.batch;

		handleInput(renderer);

		renderDarkBackground(batch);

		if (shouldRenderPlayerInvTitle) renderContainerTitle(renderer,
				Translator.getMsg("inventory.playerInv.name"), 32, 64);

		for (int i = 0; i < slots.size; i++) {
			Slot slot = slots.get(i);
			int slotState = calculateSlotState(slot);

			slot.render(renderer, slotState);
		}

		if (logic.mouseStack.isNothing()) {
			for (int i = 0; i < slots.size; i++) {
				Slot slot = slots.get(i);
				ItemStack stack = slot.inventory.getSlot(slot.slotNum);

				if (slot.isMouseOver()) {
					if (stack.isNothing()) continue;

					renderer.main.drawTextBg(renderer.main.font,
							stack.getItem().getLocalizedName(stack) + " x" + stack.getAmount(),
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
			if (Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isButtonPressed(Buttons.RIGHT)) {
				state |= SlotState.MOUSE_BUTTON_CLICKED;
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
					packet.invId = slot.inventory.invId;
					packet.invX = slot.inventory.invX;
					packet.invY = slot.inventory.invY;

					// the button used
					packet.buttonUsed = Buttons.LEFT;

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
					packet.invId = slot.inventory.invId;
					packet.invX = slot.inventory.invX;
					packet.invY = slot.inventory.invY;

					// the button used
					packet.buttonUsed = Buttons.RIGHT;

					// stacks and slot (server handles actual logic)
					packet.mouseStack = renderer.logic.mouseStack;
					packet.slotToSwap = slot.slotNum;

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
		for (int i = 0; i < InventoryPlayer.MAX_INV_CAPACITY; i++) {
			int x = i % 9;
			int y = i / 9;
			slots.add(new Slot(this.playerInv, i, (TEMPLATE_SLOT.width / 2)
					+ (x * TEMPLATE_SLOT.width) + (x * 4), Settings.DEFAULT_HEIGHT
					- (TEMPLATE_SLOT.height * 2) - (y * 4) - (y * TEMPLATE_SLOT.height)));
		}
	}

	/**
	 * Render a container title. Text origin is top left, coordinate origin is TOP left. Font height included.
	 * @param title
	 * @param x
	 * @param y
	 */
	public void renderContainerTitle(WorldRenderer renderer, String title, float x, float y) {
		renderer.main.font.setColor(1, 1, 1, 1);
		renderer.main.font.draw(renderer.batch, title, x,
				Main.convertY(y - (renderer.main.font.getBounds(title).height) - 4));
	}
	
	public void setShouldRenderPlayerInvTitle(boolean b){
		shouldRenderPlayerInvTitle = b;
	}

}
