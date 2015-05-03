package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.entity.EntityLiving;
import projectmp.server.ServerLogic;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Connection;


public class PacketUpdateHealth implements Packet{

	public long uuid = 0;
	public int newhealth = 1;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		for(int i = 0; i < logic.world.entities.size; i++){
			if(logic.world.entities.get(i).uuid == uuid){
				if(logic.world.entities.get(i) instanceof EntityLiving){
					EntityLiving e = (EntityLiving) logic.world.entities.get(i);
					e.health = MathUtils.clamp(newhealth, 0, e.maxhealth);
				}
			}
		}
	}

}
