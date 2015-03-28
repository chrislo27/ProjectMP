package projectmp.client;

import projectmp.common.Main;
import projectmp.common.packet.Packet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class ClientListener extends Listener{

	Main main;
	
	public ClientListener(Main m){
		main = m;
	}
	
	@Override
	public void connected(Connection connection){
		
	}
	
	@Override
	public void disconnected(Connection connection){
		
	}
	
	@Override
	/**
	 * Hands off to another method depending on object type. Also a bit cleaner for code in case a certain packet class type must
	 * be handled externally
	 */
	public void received(Connection connection, Object obj){
		if(obj instanceof Packet){
			handlePackets(connection, obj);
		}
	}
	
	private void handlePackets(Connection connection, Object obj){
		((Packet) obj).actionClient(connection, main);
	}
	
}
