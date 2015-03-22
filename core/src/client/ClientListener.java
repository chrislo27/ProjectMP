package client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import common.packet.Packet;


public class ClientListener extends Listener{

	@Override
	public void connected(Connection connection){
		
	}
	
	@Override
	public void received(Connection connection, Object obj){
		if(obj instanceof Packet){
			((Packet) obj).actionClient(connection);
		}
	}
	
	@Override
	public void disconnected(Connection connection){
		
	}
	
}
