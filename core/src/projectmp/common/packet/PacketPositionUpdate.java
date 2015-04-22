package projectmp.common.packet;

import projectmp.common.Main;
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
	public void actionClient(Connection connection, Main main) {
		if(Main.GAME.world == null) return;

		for(int i = 0; i < Main.GAME.world.entities.size; i++){
			for(int key = 0; key < entityid.length; key++){
				if(Main.GAME.world.entities.get(i).uuid == entityid[key]){
					if(Main.GAME.world.entities.get(i) instanceof EntityPlayer){
						if(Main.GAME.getPlayer().uuid == entityid[key]){
							// skip own player because that's locked to client
							continue;
						}
					}
					Main.GAME.world.entities.get(i).velox = velox[key];
					Main.GAME.world.entities.get(i).veloy = veloy[key];
					Main.GAME.world.entities.get(i).positionUpdate(x[key], y[key]);
					
					break;
				}
			}
		}
	}

}
