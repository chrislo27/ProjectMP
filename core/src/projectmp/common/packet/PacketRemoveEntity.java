package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketRemoveEntity implements Packet {

	public long uuid;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		for(int i = 0; i < logic.world.entities.size; i++){
			if(logic.world.entities.get(i).uuid == uuid){
				logic.world.entities.removeIndex(i);
				return;
			}
		}
	}

}
