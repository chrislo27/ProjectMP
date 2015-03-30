package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet4PositionUpdate implements Packet {

	public long entityid;
	public float x, y;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		if(Main.GAME.world == null) return;
		for(int i = 0; i < Main.GAME.world.entities.size; i++){
			if(Main.GAME.world.entities.get(i).uuid == entityid){
				Main.GAME.world.entities.get(i).positionUpdate(x, y);
				//Main.logger.debug("found " + entityid + " at pos " + x + ", " + y);
				break;
			}
		}
	}

}
