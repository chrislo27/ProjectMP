package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityLiving;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketUpdateHealth implements Packet {

	public long uuid = 0;
	public int newhealth = 1;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		Entity e = logic.world.getEntityByUUID(uuid);
		
		if(e == null) return;
		if(!(e instanceof EntityLiving)) return;
		
		((EntityLiving) e).health = newhealth;
	}

}
