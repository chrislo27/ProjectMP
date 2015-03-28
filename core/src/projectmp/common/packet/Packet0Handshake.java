package projectmp.common.packet;

import projectmp.common.Main;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;


public class Packet0Handshake implements Packet{

	
	public static final int ACCEPTED = 1;
	public static final int REQUEST = 2;
	public static final int REJECTED_OUT_OF_DATE = 3;
	public static final int REJECTED_DUPLICATE_NAME = 4;
	public static final int REJECTED_BANNED = 5;
	public static final int REJECTED_MAX_PLAYERS = 6;
	public static final int REJECTED_INVALID_PACKET = 7;
	public static final int REJECTED_BAD_USERNAME = 8;
	
	int state = REQUEST;
	String version = "";
	String username = null;
	
	public Packet0Handshake(){
		version = Main.version + "";
		username = Main.username + "";
	}
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		Packet0Handshake returner = new Packet0Handshake();
		returner.state = ACCEPTED;
		
		if(!version.equalsIgnoreCase(Main.version)){
			returner.state = REJECTED_OUT_OF_DATE;
		}else if(username == null || username.equals("")){
			returner.state = REJECTED_BAD_USERNAME;
		}
		
		connection.sendTCP(returner);
	}

	@Override
	public void actionClient(Connection connection, Main main) {
		if(state == ACCEPTED){
			
		}else if(state >= REJECTED_OUT_OF_DATE){
			
		}
	}

}
