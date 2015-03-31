package projectmp.server;

import projectmp.common.Main;
import projectmp.common.packet.Packet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener {
	
	ServerLogic logic;
	
	public ServerListener(ServerLogic s){
		logic = s;
	}

	@Override
	public void connected(Connection connection) {
		Main.logger.info(connection.toString() + " (" + connection.getRemoteAddressTCP().toString() + ") connected");
	}

	@Override
	public void disconnected(Connection connection) {
		Main.logger.info(connection.toString() + " disconnected");
		logic.removePlayer(connection.getID());
	}

	@Override
	/**
	 * Hands off to another method depending on object type. Also a bit cleaner for code in case a certain packet class type must
	 * be handled externally
	 */
	public void received(Connection connection, Object obj) {
		if (obj instanceof Packet) {
			handlePackets(connection, obj);
		}
	}

	private void handlePackets(Connection connection, Object obj) {
		((Packet) obj).actionServer(connection, logic);
	}
}
