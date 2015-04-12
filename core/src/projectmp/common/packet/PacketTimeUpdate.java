package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.world.Time;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketTimeUpdate implements Packet{

	public int totalTicks = 0;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		Main.GAME.world.worldTime.setTotalTime(totalTicks);
	}

}
