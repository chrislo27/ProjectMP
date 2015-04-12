package projectmp.common.packet;

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
	public void actionClient(Connection connection, Main main) {
		for(int i = 0; i < Main.GAME.world.entities.size; i++){
			if(Main.GAME.world.entities.get(i).uuid == uuid){
				if(Main.GAME.world.entities.get(i) instanceof EntityLiving){
					EntityLiving e = (EntityLiving) Main.GAME.world.entities.get(i);
					e.health = MathUtils.clamp(newhealth, 0, e.maxhealth);
				}
			}
		}
	}

}
