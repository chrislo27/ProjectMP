package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.server.ServerLogic;
import projectmp.server.player.ServerPlayer;

import com.esotericsoftware.kryonet.Connection;

/**
 * client sends this to the server
 * 
 *
 */
public class PacketItemUse implements Packet{

	public static final int ON_START = 0;
	public static final int ON_END = 1;
	
	/**
	 * The status (start or end). By default ON_END (safer than ON_START).
	 */
	public int status = ON_END;
	
	public int selectedSlot = 0;
	
	/**
	 * Cursor positions
	 */
	public int cursorX, cursorY;
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		ServerPlayer sp = logic.getServerPlayerByName(connection.toString());
		
		if(sp == null) return;
		
		if(status == ON_START){
			sp.startUsingItem(logic, selectedSlot, cursorX, cursorY);
		}else if(status == ON_END){
			sp.stopUsingItem(logic, cursorX, cursorY);
		}
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
	}

}
