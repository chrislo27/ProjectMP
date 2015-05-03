package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.block.Blocks;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketBlockUpdate implements Packet {

	public String block;
	public int meta;
	
	public int x = 0;
	public int y = 0;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		logic.world.setBlock(Blocks.instance().getBlock(block), x, y);
		logic.world.setMeta(meta, x, y);
	}

}
