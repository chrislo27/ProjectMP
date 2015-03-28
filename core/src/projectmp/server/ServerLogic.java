package projectmp.server;

import projectmp.common.Main;
import projectmp.common.world.World;

import com.esotericsoftware.kryonet.Server;


public class ServerLogic {

	public Main main;
	public Server server;
	
	public World world = null;
	
	public int maxplayers = 1;
	
	public ServerLogic(Main m){
		main = m;
		server = main.server;
		
		world = new World(main, 16, 16);
	}
	
	public void tickUpdate(){
		world.tickUpdate();
	}
}
