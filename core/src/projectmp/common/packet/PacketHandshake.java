package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.world.World;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketHandshake implements Packet {

	public static final int ACCEPTED = 1;
	public static final int REQUEST = 2;
	public static final int REJECTED = 3;

	int state = REQUEST;
	public String version = "";
	public String username = null;
	String rejectReason = "unknown reason";
	int worldsizex, worldsizey;
	long seed;

	public PacketHandshake() {

	}

	private void reject(String reason, PacketHandshake returner) {
		returner.state = REJECTED;
		returner.rejectReason = reason;
	}

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		PacketHandshake returner = new PacketHandshake();
		returner.state = ACCEPTED;
		returner.worldsizex = logic.world.sizex;
		returner.worldsizey = logic.world.sizey;
		returner.seed = logic.world.seed;

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
		} else if (returner.state == ACCEPTED) {
			// name the connection the player's name
			connection.setName(username);

			// create the new player entity
			EntityPlayer newPlayer = new EntityPlayer(logic.world, 0, 0);
			newPlayer.username = username;
			logic.world.entities.add(newPlayer);

			// send the entire world, and entities (includes player)
			logic.sendEntireWorld(connection);
			logic.sendEntities(connection);

			// tell everyone else about the new player
			PacketNewEntity everyone = new PacketNewEntity();
			everyone.e = newPlayer;
			logic.server.sendToAllExceptTCP(connection.getID(), everyone);
			
			// update the time (for everyone)
			logic.world.sendTimeUpdate();

			Main.logger.info("Finished handshake for " + username + " (" + connection.getRemoteAddressTCP().toString() + ", conn. name is " + connection.toString() + ")");
		}
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		if (state == ACCEPTED) {
			Main.GAME.newWorld(new World(main, worldsizex, worldsizey, false, seed));
			Main.CONNECTING.setMessage("Receiving world data: ");
		} else if (state == REJECTED) {
			Main.ERRORMSG.setMessage("Failed to connect:\n" + rejectReason);
			main.setScreen(Main.ERRORMSG);
			connection.close();
		}
	}

}
