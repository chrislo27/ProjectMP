package projectmp.common.packet;

import projectmp.client.ClientLogic;
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
	
	/**
	 * The item stack.
	 */
	public ItemStack stack = new ItemStack(null, 0);
	
	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
		if(stack == null) return;
		
		ServerPlayer sp = logic.getServerPlayerByName(connection.toString());
		
		if(sp == null) return;
		
		if(status == ON_START){
			sp.startUsingItem(logic, stack);
		}else if(status == ON_END){
			sp.stopUsingItem(logic);
		}
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
	}

}
