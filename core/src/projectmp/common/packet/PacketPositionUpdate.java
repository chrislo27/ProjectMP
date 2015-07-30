package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketPositionUpdate implements Packet {

	public long[] entityid = new long[1];
	public float[] x = new float[1];
	public float[] y = new float[1];
	public float[] velox = new float[1];
	public float[] veloy = new float[1];
	public int size = 1;

	public void resetTables(int newlength) {
		entityid = new long[newlength];
		x = new float[newlength];
		y = new float[newlength];
		velox = new float[newlength];
		veloy = new float[newlength];
		size = newlength;
	}

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if (logic.world == null) return;
		
		for (int key = 0; key < size; key++) {
			Entity e = logic.world.getEntityByUUID(entityid[key]);
			
			if(e == null) continue;
			
			// skip client's player b/c position is client-side
			if(e instanceof EntityPlayer && entityid[key] == logic.getPlayer().uuid) continue;
			
			e.velox = velox[key];
			e.veloy = veloy[key];
			e.positionUpdate(x[key], y[key]);
		}
	}

}
