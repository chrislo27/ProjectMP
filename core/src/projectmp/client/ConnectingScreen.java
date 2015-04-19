package projectmp.client;

import java.io.IOException;

import projectmp.common.Main;
import projectmp.common.packet.PacketHandshake;


public class ConnectingScreen extends MessageScreen{

	public ConnectingScreen(Main m) {
		super(m);
	}
	
	public void connectTo(final String host, final int port){
		setMessage("Closing client (if still connected)");
		main.client.stop();
		setMessage("Attempting to connect to " + host + ":" + port);
		main.client.start();
		new Thread(){
			
			@Override
			public void run(){
				try {
					main.client.connect(5000, host, port, port);
					Main.logger.info("Successfully connected to " + host + ":" + port);
					setMessage("Connected to server; sending handshake");
					
					PacketHandshake handshake = new PacketHandshake();
					handshake.username = Main.username + "";
					handshake.version = Main.version + "";
					main.client.sendTCP(handshake);
				} catch (Exception e) {
					e.printStackTrace();
					setMessage("");
					Main.ERRORMSG.setMessage("Failed to connect:\n" + e.toString());
					main.setScreen(Main.ERRORMSG);
				}
			}
			
		}.start();
	}
	
	@Override
	public void renderUpdate() {
		
	}
	
}
