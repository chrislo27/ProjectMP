package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.entity.EntityPlayer;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

/**
 * client sends this to the server
 * if the server sends this to a client they must update the position
 * server uses PacketPositionUpdate to update ALL entities, regardless of type
 *
 */
public class PacketPlayerPosUpdate implements Packet {

	public String username;
	public float x, y;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		if(username == null) return;
		if(logic.getConnectionIDByName(username) != connection.getID()) return;
		if(logic.getPlayerByName(username) == null) return;
		
		EntityPlayer p = logic.getPlayerByName(username);
		p.x = x;
		p.y = y;
		
		PacketPositionUpdate shared = logic.getSharedPosUpdatePacket();
		shared.entityid[0] = p.uuid;
		shared.x[0] = p.x;
		shared.y[0] = p.y;
		
		logic.server.sendToAllExceptUDP(connection.getID(), shared);
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		if(username.equals(Main.username)){
			if(Main.GAME.getPlayer() != null){
				Main.GAME.getPlayer().positionUpdate(x, y);
			}
		}
	}

}
