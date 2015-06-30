package projectmp.common.packet.handshake;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.packet.Packet;
import projectmp.common.packet.PacketNewEntity;
import projectmp.common.world.World;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

import static projectmp.common.packet.handshake.HandshakeRejector.ACCEPTED;
import static projectmp.common.packet.handshake.HandshakeRejector.REJECTED;
import static projectmp.common.packet.handshake.HandshakeRejector.REQUEST;

public class PacketHandshake implements Packet {

	int state = REQUEST;
	public String version = "";
	public String username = null;
	String rejectReason = "unknown reason";
	int worldsizex, worldsizey;
	long seed;

	public PacketHandshake() {

	}
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		PacketHandshake returner = new PacketHandshake();
		returner.state = ACCEPTED;
		returner.worldsizex = logic.world.sizex;
		returner.worldsizey = logic.world.sizey;
		returner.seed = logic.world.seed;

		HandshakeRejector.attemptReject(logic, returner, username, version);

		connection.sendTCP(returner);

		if (returner.state == REJECTED) {
			connection.close();
			
			Main.logger.info("Kicking " + connection.toString() + " ("
					+ connection.getRemoteAddressTCP().toString() + ") for: "
					+ returner.rejectReason);
			return;
		} else if (returner.state == ACCEPTED) {
			HandshakeAcceptor.sendEssentialData(connection, logic, username);

			Main.logger.info("Finished handshake for " + username + " ("
					+ connection.getRemoteAddressTCP().toString() + ", conn. name is "
					+ connection.toString() + ")");
		}
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if (state == ACCEPTED) {
			logic.newWorld(new World(logic.main, worldsizex, worldsizey, false, seed));
			Main.CONNECTING.setMessage("Receiving world data: ");
		} else if (state == REJECTED) {
			Main.ERRORMSG.setMessage("Failed to connect:\n" + rejectReason);
			logic.main.setScreen(Main.ERRORMSG);
			connection.close();
		}
	}

}
