package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.entity.EntityPlayer;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class PacketPositionUpdate implements Packet {

	public long[] entityid = new long[1];
	public float[] x = new float[1];
	public float[] y = new float[1];
	public float[] velox = new float[1];
	public float[] veloy = new float[1];
	
	public void resetTables(int newlength){
		entityid = new long[newlength];
		x = new float[newlength];
		y = new float[newlength];
		velox = new float[newlength];
		veloy = new float[newlength];
	}
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if(logic.world == null) return;

		for(int i = 0; i < logic.world.entities.size; i++){
			for(int key = 0; key < entityid.length; key++){
				if(logic.world.entities.get(i).uuid == entityid[key]){
					if(logic.world.entities.get(i) instanceof EntityPlayer){
						if(logic.getPlayer().uuid == entityid[key]){
							// skip own player because that's locked to client
							continue;
						}
					}
					logic.world.entities.get(i).velox = velox[key];
					logic.world.entities.get(i).veloy = veloy[key];
					logic.world.entities.get(i).positionUpdate(x[key], y[key]);
					
					break;
				}
			}
		}
	}

}
