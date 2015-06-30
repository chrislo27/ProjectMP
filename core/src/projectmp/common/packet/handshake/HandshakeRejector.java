package projectmp.common.packet.handshake;

import projectmp.common.Main;
import projectmp.server.ServerLogic;


public final class HandshakeRejector {

	public static final int ACCEPTED = 1;
	public static final int REQUEST = 2;
	public static final int REJECTED = 3;
	
	private HandshakeRejector(){}
	
	/**
	 * @return true if rejected
	 */
	public static final boolean attemptReject(ServerLogic logic, PacketHandshake returner, String username, String version){
		if (!version.equalsIgnoreCase(Main.version)) {
			reject("Incompatible versions (server is " + Main.version + ")", returner);
		} else if (username == null || username.equals("")) {
			reject("Invalid username", returner);
		} else if (logic.server.getConnections().length > logic.maxplayers) {
			reject("Max player limit reached (limit: " + logic.maxplayers + ")", returner);
		}
		
		return false;
	}
	
	private static void reject(String reason, PacketHandshake returner) {
		returner.state = REJECTED;
		returner.rejectReason = reason;
	}

	
}
