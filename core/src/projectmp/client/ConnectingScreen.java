package projectmp.client;

import java.io.IOException;

import com.esotericsoftware.minlog.Log;

import projectmp.common.Main;
import projectmp.common.packet.PacketHandshake;


public class ConnectingScreen extends MessageScreen{

	public ConnectingScreen(Main m) {
		super(m);
	}

	public void connectTo(final String host, final int port) {
		setMessage("Attempting to connect to " + host + ":" + port);
		
		Thread connector = new Thread(){
			
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
			
		};
		connector.setDaemon(true);
		connector.start();
	}
	
	@Override
	public void renderUpdate() {
		
	}
	
}
