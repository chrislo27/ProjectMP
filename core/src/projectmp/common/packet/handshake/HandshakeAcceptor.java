package projectmp.common.packet.handshake;

import projectmp.common.entity.EntityPlayer;
import projectmp.common.packet.PacketNewEntity;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public final class HandshakeAcceptor {

	private HandshakeAcceptor() {
	}

	public static void sendEssentialData(Connection connection, ServerLogic logic,
			String username) {
		// name the connection the player's name
		connection.setName(username);

		// create the new player entity
		EntityPlayer newPlayer = new EntityPlayer(logic.world, logic.world.sizex / 2, 0);
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
	}

}
