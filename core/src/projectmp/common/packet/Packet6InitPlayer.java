package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.entity.EntityPlayer;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet6InitPlayer implements Packet {

	EntityPlayer thePlayer;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		thePlayer.world = Main.GAME.world;
		Main.GAME.player = thePlayer;
	}

}
