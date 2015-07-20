package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.packet.repository.PacketRepository;
import projectmp.common.util.Utils;
import projectmp.server.ServerLogic;
import projectmp.server.player.ServerPlayer;

import com.esotericsoftware.kryonet.Connection;


public class PacketUpdateCursor implements Packet{

	public int x, y;
	public String username;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		ServerPlayer sp = logic.getServerPlayerByName(connection.toString());
		
		if(sp == null) return;
		
		sp.setCursor(x, y);
		
		logic.server.sendToAllExceptUDP(connection.getID(), this);
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if(username == null) return;
		if(Main.username.equals(username)) return;
		
		logic.putOtherPlayerCursor(username, Utils.packLong(x, y));
	}

}
