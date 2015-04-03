package projectmp.client;

import java.io.IOException;

import projectmp.common.Main;
import projectmp.common.packet.Packet0Handshake;


public class ConnectingScreen extends MessageScreen{

	public ConnectingScreen(Main m) {
		super(m);
	}

	public float percentReceived = 0f;
	
	public void connectTo(String host, int port){
		percentReceived = 0;
		setMessage("Closing client (if still connected)");
		main.client.close();
		setMessage("Attempting to connect to " + host + ":" + port);
		try {
			main.client.connect(5000, host, port, port);
			Main.logger.info("Successfully connected to " + host + ":" + port);
			setMessage("Connected to server; sending handshake");
			
			Packet0Handshake handshake = new Packet0Handshake();
			handshake.username = Main.username + "";
			handshake.version = Main.version + "";
			main.client.sendTCP(handshake);
		} catch (IOException e) {
			e.printStackTrace();
			setMessage("");
			Main.ERRORMSG.setMessage("Failed to connect:\n" + e.getMessage());
			main.setScreen(Main.ERRORMSG);
		}
	}
	
	@Override
	public void renderUpdate() {
		
	}
	
}
