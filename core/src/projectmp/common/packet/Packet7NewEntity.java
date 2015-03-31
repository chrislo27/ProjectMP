package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.entity.Entity;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet7NewEntity implements Packet {

	Entity e;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		Main.GAME.world.entities.add(e);
	}

}
