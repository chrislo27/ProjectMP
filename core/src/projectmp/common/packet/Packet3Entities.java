package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.common.entity.Entity;
import projectmp.server.ServerLogic;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;


public class Packet3Entities implements Packet {

	public Array<Entity> entities;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
	}

}
