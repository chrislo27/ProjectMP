package projectmp.common.packet;

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
	public void actionClient(Connection connection, Main main) {
		for(int x = 0; x < 16; x++){
			for(int y = 0; y < 16; y++){
				Main.GAME.world.setBlock(Blocks.instance().getBlock(blocks[x][y]), x, y);
				Main.GAME.world.setMeta(meta[x][y], x, y);
			}
		}
		
		Main.WORLDGETTING.addPercent();
	}

}
