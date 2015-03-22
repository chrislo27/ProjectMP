package common.packet;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Connection;


public class Packet0KeepAlive implements Packet{

	long randomNumber = MathUtils.random(Long.MIN_VALUE, Long.MAX_VALUE);
	
	@Override
	public void actionClient(Connection connection) {
	}

	@Override
	public void actionServer(Connection connection) {
		
	}

}
