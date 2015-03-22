package common.packet;

import com.badlogic.gdx.math.MathUtils;


public class Packet0KeepAlive implements Packet{

	long randomNumber = MathUtils.random(Long.MIN_VALUE, Long.MAX_VALUE);
	
	@Override
	public void action() {
	}

}
