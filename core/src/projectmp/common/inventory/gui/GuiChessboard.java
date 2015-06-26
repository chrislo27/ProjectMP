package projectmp.common.inventory.gui;

import projectmp.common.Settings;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryChessboard;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Slot.SlotState;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.world.World;


public class GuiChessboard extends Gui{

	public GuiChessboard(World world, InventoryPlayer player, String id, int invx, int invy) {
		super(world, player, id, invx, invy);
		
		addPlayerInventory();
		setUnlocalizedName("chessboard");
		
		Inventory inv = GuiRegistry.instance().getInventory(id, world, invx, invy);
		
		for(int y = 0; y < 8; y++){
			for(int x = 0; x < 8; x++){
				slots.add(new Slot(inv, y * 8 + x, (TEMPLATE_SLOT.width / 2) + (x * TEMPLATE_SLOT.width)
						+ (x * 4), (int) (Settings.DEFAULT_HEIGHT - (TEMPLATE_SLOT.height * 3.5f) - (y * 4) - (y * TEMPLATE_SLOT.height))));
			}
		}
	}
	
	@Override
	protected int calculateSlotState(Slot slot){
		int state = super.calculateSlotState(slot);
		
		if(slot.slotNum % 2 != ((slot.slotNum / 8) % 2) && slot.inventory instanceof InventoryChessboard){
			state |= SlotState.DARKEN;
		}
		
		return state;
	}

}
