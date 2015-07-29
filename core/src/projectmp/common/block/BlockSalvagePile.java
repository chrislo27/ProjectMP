package projectmp.common.block;

import projectmp.common.block.droprate.DropRate;


public class BlockSalvagePile extends Block {

	public BlockSalvagePile(String unlocalName) {
		super(unlocalName);
	}

	@Override
	public void initializeDroppedItems(){
		getDroppedItems().clear();
		
		getDroppedItems().add(new DropRate("scrapMetal", 1, 1, 1f));
	}
	
}
