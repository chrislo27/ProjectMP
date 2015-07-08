package projectmp.server.player;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.io.CanBeSavedToNBT;

/**
 * Only used by the server instance to keep track of player position, inventory, etc. This is also why EntityPlayer implements Unsaveable.
 * 
 *
 */
public class ServerPlayer implements CanBeSavedToNBT{

	public String username = null;
	public InventoryPlayer inventory = new InventoryPlayer();
	public float posx;
	public float posy;
	
	public ServerPlayer(String username){
		this.username = username;
	}

	@Override
	public void writeToNBT(TagCompound tag) {
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
	}
	
}
