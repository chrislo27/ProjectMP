package projectmp.common.packet;

import projectmp.common.Main;
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
	public void actionClient(Connection connection, Main main) {
		Main.GAME.world.entities = new Array(entities);
		for(Entity e : Main.GAME.world.entities){
			e.world = Main.GAME.world;
		}
	}

}
