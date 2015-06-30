package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketEndChunkTransfer implements Packet {

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		Main.logger.info("Finished receiving chunk data from the server");
		
		logic.world.lightingEngine.doLightingUpdates(0, 0, logic.world.sizex, logic.world.sizey);
		
		logic.main.setScreen(Main.GAME);
	}

}
