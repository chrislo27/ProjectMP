package projectmp.common.packet;

import projectmp.client.GameScreen;
import projectmp.common.Main;
import projectmp.common.entity.EntityPlayer;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet4PositionUpdate implements Packet {

	public long[] entityid = new long[1];
	public float[] x = new float[1];
	public float[] y = new float[1];
	
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
						if(((EntityPlayer) Main.GAME.world.entities.get(i)).username.equals(Main.username)){
							continue;
						}
					}
					Main.GAME.world.entities.get(i).positionUpdate(x[key], y[key]);
					
					break;
				}
			}
		}
	}

}
