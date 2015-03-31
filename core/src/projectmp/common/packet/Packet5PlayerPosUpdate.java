package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.entity.EntityPlayer;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet5PlayerPosUpdate implements Packet {

	String username;
	float x, y;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		if(username == null) return;
		if(logic.getConnectionIDByName(username) != connection.getID()) return;
		if(logic.getPlayerByName(username) == null) return;
		
		EntityPlayer p = logic.getPlayerByName(username);
		p.x = x;
		p.y = y;
		
		Packet4PositionUpdate shared = logic.getSharedPosUpdatePacket();
		shared.entityid = p.uuid;
		shared.x = p.x;
		shared.y = p.y;
		
		logic.server.sendToAllExceptUDP(connection.getID(), shared);
	}

	@Override
	public void actionClient(Connection connection, Main main) {
	}

}
