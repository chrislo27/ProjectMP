package projectmp.server;

import projectmp.common.Main;

import com.esotericsoftware.kryonet.Server;


public class ServerLogic {

	Main main;
	Server server;
	
	public ServerLogic(Main m){
		main = m;
		server = main.server;
	}
	
	public void tickUpdate(){
		
	}
}
