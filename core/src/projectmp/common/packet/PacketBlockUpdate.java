package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.block.Blocks;
import projectmp.common.item.ItemMineable;
import projectmp.server.ServerLogic;
import projectmp.server.player.ServerPlayer;

import com.esotericsoftware.kryonet.Connection;

public class PacketBlockUpdate implements Packet {

	public String block;
	public int meta;

	public int x = 0;
	public int y = 0;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		ServerPlayer sp = logic.getServerPlayerByName(connection.toString());

		if (sp.isUsingItem()) {
			if (!sp.getCurrentUsingItem().isNothing()) {
				if (sp.getCurrentUsingItem().getItem() instanceof ItemMineable) {
					if(block.equals(Blocks.airBlock)){
						logic.world.getBlock(x, y).onBreak(logic.world, x, y);
					}
					
					logic.world.setBlock(Blocks.instance().getBlock(block), x, y);
					logic.world.setMeta(meta, x, y);
				}
			}
		}
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		logic.world.setBlock(Blocks.instance().getBlock(block), x, y);
		logic.world.setMeta(meta, x, y);
	}

}
