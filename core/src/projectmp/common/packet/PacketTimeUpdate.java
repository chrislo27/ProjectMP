package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketTimeUpdate implements Packet{

	public int totalTicks = 0;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		logic.world.worldTime.setTotalTime(totalTicks);
	}

}
