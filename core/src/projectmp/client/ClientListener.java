package projectmp.client;

import projectmp.common.Main;
import projectmp.common.packet.Packet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener {

	Main main;

	public ClientListener(Main m) {
		main = m;
	}

	@Override
	public void connected(Connection connection) {
		main.client.updateReturnTripTime();
	}

	@Override
	public void disconnected(Connection connection) {

	}

	@Override
	public void received(Connection connection, Object obj) {
		if (!(obj instanceof Packet)) return;

		main.client.updateReturnTripTime();
		((Packet) obj).actionClient(connection, main.clientLogic);
	}

}
