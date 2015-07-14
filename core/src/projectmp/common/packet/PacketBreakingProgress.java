package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.packet.repository.PacketRepository;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketBreakingProgress implements Packet {

	public int x, y;
	public float progress;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		PacketBreakingProgress packet = PacketRepository.instance().breakingProgress;

		packet.x = x;
		packet.y = y;
		packet.progress = logic.world.getBreakingProgress(x, y);
		
		if(packet.progress <= 0){
			logic.server.sendToAllExceptTCP(connection.getID(), packet);
		}else{
			logic.server.sendToAllExceptUDP(connection.getID(), packet);
		}
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		logic.world.setBreakingProgress(x, y, progress);
	}

}
