package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.error.InvalidPacketException;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

/**
 * tells client to open/close a gui
 * 
 *
 */
public class PacketGuiState implements Packet{

	public boolean shouldOpen = true;
	public String guiId = null;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}
	
	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if(shouldOpen == false){
			// server telling client it should be closed/doesn't exist anymore
			logic.setCurrentGui(null);
		}else{
			if(guiId == null){
				throw new InvalidPacketException("guiId in " + this.getClass().getSimpleName() + " cannot be null");
			}
			
		}
	}
	
}
