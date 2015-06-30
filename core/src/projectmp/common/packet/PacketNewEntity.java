package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.entity.Entity;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

/**
 * Server uses this to tell other entities to make a new one.
 * 
 *
 */
public class PacketNewEntity implements Packet {

	public Entity e;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		e.world = logic.world;
		logic.world.createNewEntity(e);
	}

}
