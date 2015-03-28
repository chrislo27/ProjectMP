package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class Packet0Handshake implements Packet {

	public static final int ACCEPTED = 1;
	public static final int REQUEST = 2;
	public static final int REJECTED = 3;

	int state = REQUEST;
	public String version = "";
	public String username = null;
	String rejectReason = "unknown reason";

	public Packet0Handshake() {

	}

	private void reject(String reason, Packet0Handshake returner) {
		returner.state = REJECTED;
		returner.rejectReason = reason;
	}

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		Packet0Handshake returner = new Packet0Handshake();
		returner.state = ACCEPTED;

		if (!version.equalsIgnoreCase(Main.version)) {
			reject("Incompatible versions (server is " + Main.version + ")", returner);
		} else if (username == null || username.equals("")) {
			reject("Invalid username", returner);
		} else if (logic.server.getConnections().length > logic.maxplayers) {
			reject("Max player limit reached (limit: " + logic.maxplayers + ")", returner);
		}

		connection.sendTCP(returner);
		
		if (returner.state == REJECTED) {
			Main.logger.info("Kicking " + connection.toString() + " ("
					+ connection.getRemoteAddressTCP().toString() + ") for: "
					+ returner.rejectReason);
			connection.close();
			return;
		}else if(returner.state == ACCEPTED){
			// send the entire world, and entities
		}
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		if (state == ACCEPTED) {
			main.setScreen(Main.GAME);
		} else if (state == REJECTED) {
			Main.ERRORMSG.setMessage("Failed to connect:\n" + rejectReason);
			main.setScreen(Main.ERRORMSG);
			connection.close();
		}
	}

}
