package projectmp.common.packet.handshake;

import java.io.IOException;

import projectmp.common.entity.EntityPlayer;
import projectmp.common.util.FileNameUtils;
import projectmp.common.util.Utils;
import projectmp.server.ServerLogic;
import projectmp.server.player.ServerPlayer;

import com.esotericsoftware.kryonet.Connection;

public final class HandshakeAcceptor {

	private HandshakeAcceptor() {
	}

	public static void sendEssentialData(Connection connection, ServerLogic logic, String username) {
		// name the connection the player's name
		connection.setName(username);

		// send the entire world, and entities (excludes player)
		logic.sendEntireWorld(connection);
		logic.sendEntities(connection);

		// create the new player entity
		EntityPlayer newPlayer = new EntityPlayer(logic.world, logic.world.sizex / 2, 0);
		newPlayer.username = username;

		// get or create serverplayer instance
		ServerPlayer sp = logic.getServerPlayerByName(username);
		if (sp == null) {
			// create new instance
			sp = logic.createNewServerPlayer(newPlayer);
		}else{
			newPlayer.uuid = sp.getUUID();
		}
		newPlayer.setInventory(sp.inventory);
		newPlayer.positionUpdate(sp.posx, sp.posy);
		
		// send create packet
		logic.world.createNewEntity(newPlayer);

		// update the time (for everyone)
		logic.world.sendTimeUpdate();
		
		try {
			logic.save(FileNameUtils.DEFAULT_SAVE_FOLDER + "save0/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
