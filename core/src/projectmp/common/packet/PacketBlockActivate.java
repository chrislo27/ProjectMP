package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

/**
 * used by client to tell server this block was right clicked and should act
 * 
 *
 */
public class PacketBlockActivate implements Packet{

	String playerUsername = null;
	int blockX, blockY;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		if(playerUsername == null) return;
		
		logic.world.getBlock(blockX, blockY).onActivate(logic.world, blockX, blockY, logic.getPlayerByName(playerUsername));
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
	}

}
