package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketBreakingProgress implements Packet{

	public int x, y;
	public float progress;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		logic.world.setBreakingProgress(x, y, progress);
	}

}
