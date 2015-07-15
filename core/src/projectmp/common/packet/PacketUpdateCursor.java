package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.server.ServerLogic;
import projectmp.server.player.ServerPlayer;

import com.esotericsoftware.kryonet.Connection;


public class PacketUpdateCursor implements Packet{

	public int x, y;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		ServerPlayer sp = logic.getServerPlayerByName(connection.toString());
		
		if(sp == null) return;
		
		sp.cursorX = x;
		sp.cursorY = y;
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
	}

}
