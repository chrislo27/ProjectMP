package projectmp.common.inventory.gui;

import projectmp.common.Settings;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.gui.Slot.SlotState;
import projectmp.common.registry.GuiRegistry;
import projectmp.common.world.World;


public class GuiChessBoard extends Gui{

	public GuiChessBoard(World world, InventoryPlayer player, String id, int invx, int invy) {
		super(world, player, id, invx, invy);
		
		addPlayerInventory();
		setUnlocalizedName("chessboard");
		
		Inventory inv = GuiRegistry.instance().getInventory(id, world, invx, invy);
		
		for(int i = 0; i < 64; i++){
			slots.add(new Slot(inv, i, (TEMPLATE_SLOT.width / 2) + (i * TEMPLATE_SLOT.width)
					+ (i * 4), Settings.DEFAULT_HEIGHT - (TEMPLATE_SLOT.height * 2) - 86));
		}
	}
	
	@Override
	protected int calculateSlotState(Slot slot){
		int state = super.calculateSlotState(slot);
		
		if(slot.slotNum % 2 == 1){
			state |= SlotState.DARKEN;
		}
		
		return state;
	}

}
