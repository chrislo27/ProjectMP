package projectmp.common.packet;

import projectmp.client.ClientLogic;
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

	private static final PacketPositionUpdate playerPosPacket = new PacketPositionUpdate();
	
	public String username;
	public float x, y;
	public float velox, veloy;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		if(username == null) return;
		if(logic.getConnectionIDByName(username) != connection.getID()) return;
		if(logic.getPlayerByName(username) == null) return;
		
		EntityPlayer p = logic.getPlayerByName(username);
		p.x = x;
		p.y = y;
		p.velox = velox;
		p.veloy = veloy;
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if(username.equals(Main.username)){
			if(logic.getPlayer() != null){
				logic.getPlayer().velox = velox;
				logic.getPlayer().veloy = veloy;
				logic.getPlayer().positionUpdate(x, y);
			}
		}
	}

}
