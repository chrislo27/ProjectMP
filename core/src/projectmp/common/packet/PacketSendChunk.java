package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.block.Blocks;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketSendChunk implements Packet{

	public String[][] blocks = new String[16][16];
	public int[][] meta = new int[16][16];
	
	public int originx = 0;
	public int originy = 0;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		for(int x = 0; x < 16; x++){
			for(int y = 0; y < 16; y++){
				logic.world.setBlock(Blocks.instance().getBlock(blocks[x][y]), originx + x, originy + y);
				logic.world.setMeta(meta[x][y], originx + x, originy + y);
			}
		}
		
		Main.WORLDGETTING.addPercent();
	}

}
