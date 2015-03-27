package projectmp.common.packet;

import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public interface Packet {
	
	public void actionServer(Connection connection, ServerLogic logic);
	
	public void actionClient(Connection connection);
	
}
