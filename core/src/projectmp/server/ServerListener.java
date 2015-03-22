package projectmp.server;

import projectmp.common.packet.Packet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class ServerListener extends Listener{

	@Override
	public void connected(Connection connection){
		
	}
	
	@Override
	public void received(Connection connection, Object obj){
		if(obj instanceof Packet){
			((Packet) obj).actionServer(connection);
		}
	}
	
	@Override
	public void disconnected(Connection connection){
		
	}
}
