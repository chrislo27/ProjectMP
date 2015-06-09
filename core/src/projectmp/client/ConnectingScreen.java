package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Translator;
import projectmp.common.packet.PacketHandshake;

public class ConnectingScreen extends MessageScreen {

	public ConnectingScreen(Main m) {
		super(m);
	}

	public void connectTo(final String host, final int port) {
		setMessage(Translator.instance().getMsg("menu.msg.attemptingconnection") + host + ":"
				+ port);
		main.serverLogic.isSingleplayer = false; // this gets changed if it's singleplayer outside of this method

		Thread connector = new Thread() {

			@Override
			public void run() {
				try {
					main.client.connect(5000, host, port, port);
					Main.logger.info("Successfully connected to " + host + ":" + port);
					setMessage(Translator.instance().getMsg("menu.msg.sendinghandshake"));

					PacketHandshake handshake = new PacketHandshake();
					handshake.username = Main.username + "";
					handshake.version = Main.version + "";
					main.client.sendTCP(handshake);
				} catch (Exception e) {
					e.printStackTrace();
					setMessage("");
					Main.ERRORMSG.setMessage(Translator.instance().getMsg(
							"menu.msg.failedconnection")
							+ "\n" + e.toString());
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
