package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.entity.Entity;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketNewEntity implements Packet {

	public Entity e;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		e.world = Main.GAME.world;
		Main.GAME.world.entities.add(e);
	}

}
