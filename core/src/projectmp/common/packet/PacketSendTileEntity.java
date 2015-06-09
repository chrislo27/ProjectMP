package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.tileentity.TileEntity;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketSendTileEntity implements Packet {

	public TileEntity te;
	public int x, y;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		logic.world.setTileEntity(te, x, y);
	}

}
