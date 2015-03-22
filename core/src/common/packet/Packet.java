package common.packet;

import com.esotericsoftware.kryonet.Connection;


public interface Packet {
	
	public void actionServer(Connection connection);
	
	public void actionClient(Connection connectio);
	
}
