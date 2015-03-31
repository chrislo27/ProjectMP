package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet8RemoveEntity implements Packet {

	public long uuid;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		for(int i = 0; i < Main.GAME.world.entities.size; i++){
			if(Main.GAME.world.entities.get(i).uuid == uuid){
				Main.GAME.world.entities.removeIndex(i);
				return;
			}
		}
	}

}
