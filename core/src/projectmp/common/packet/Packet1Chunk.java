package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet1Chunk implements Packet{

	String[][] blocks = new String[16][16];
	int[][] meta = new int[16][16];
	
	int originx = 0;
	int originy = 0;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
	}

}
