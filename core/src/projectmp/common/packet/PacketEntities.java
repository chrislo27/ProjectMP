package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.entity.Entity;
import projectmp.server.ServerLogic;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;


public class PacketEntities implements Packet {

	public Entity[] entities;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		logic.world.entities = new Array(entities);
		for(Entity e : logic.world.entities){
			e.world = logic.world;
		}
	}

}
