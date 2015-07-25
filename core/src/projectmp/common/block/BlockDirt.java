package projectmp.common.block;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.world.World;

public class BlockDirt extends Block {

	public BlockDirt(String identifier) {
		super(identifier);
		this.setHardness(0.5f);
	}

}
