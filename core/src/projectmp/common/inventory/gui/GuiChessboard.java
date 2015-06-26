package projectmp.common.inventory.gui;

import projectmp.client.ClientLogic;
import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryChessboard;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Slot.SlotState;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class GuiChessboard extends Gui{

	public GuiChessboard(World world, InventoryPlayer player, String id, int invx, int invy) {
		super(world, player);
		
		addPlayerInventory();
		
		Inventory inv = GuiRegistry.instance().getInventory(id, world, invx, invy);
		
		for(int y = 0; y < 8; y++){
			for(int x = 0; x < 8; x++){
				slots.add(new Slot(inv, y * 8 + x, (int) ((TEMPLATE_SLOT.width * 11f) + (x * TEMPLATE_SLOT.width)
						+ (x * 4)), (int) ((Settings.DEFAULT_HEIGHT / 2) + 270 - TEMPLATE_SLOT.height - (y * 4) - (y * TEMPLATE_SLOT.height))));
			}
		}
		
		// captured spaces
		for (int i = 0; i < 32; i++) {
			int x = i % 8;
			int y = i / 8;
			slots.add(new Slot(inv, i + 64, (TEMPLATE_SLOT.width / 2)
					+ (x * TEMPLATE_SLOT.width) + (x * 4), (Settings.DEFAULT_HEIGHT / 2) - 92 - (y * 4) - (y * TEMPLATE_SLOT.height)));
		}
	}
	
	@Override
	public void render(WorldRenderer renderer, ClientLogic logic){
		super.render(renderer, logic);
		
		renderContainerTitle(renderer, Translator.getMsg("inventory.chessboard.name"), (TEMPLATE_SLOT.width * 11), (Settings.DEFAULT_HEIGHT / 2) - 270);
		renderContainerTitle(renderer, Translator.getMsg("inventory.chessboard.captured"), 32,
				(TEMPLATE_SLOT.height * 6));
		
	}
	
	@Override
	protected int calculateSlotState(Slot slot){
		int state = super.calculateSlotState(slot);
		
		if(slot.slotNum % 2 != ((slot.slotNum / 8) % 2) && slot.inventory instanceof InventoryChessboard && (slot.slotNum < 64)){
			state |= SlotState.DARKEN;
		}
		
		return state;
	}

}
