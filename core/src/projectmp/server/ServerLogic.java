package projectmp.server;

import projectmp.common.Main;
import projectmp.common.world.World;

import com.esotericsoftware.kryonet.Server;


public class ServerLogic {

	Main main;
	Server server;
	
	World world = null;
	
	int maxplayers = 1;
	
	public ServerLogic(Main m){
		main = m;
		server = main.server;
		
		world = new World(main, 16, 8);
	}
	
	public void tickUpdate(){
		
	}
}
