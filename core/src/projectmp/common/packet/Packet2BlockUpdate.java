package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.block.Blocks;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet2BlockUpdate implements Packet {

	String block;
	int meta;
	
	int x, y = 0;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		Main.GAME.world.setBlock(Blocks.instance().getBlock(block), x, y);
		Main.GAME.world.setMeta(meta, x, y);
	}

}
