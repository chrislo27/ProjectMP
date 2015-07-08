package projectmp.server.player;

import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.io.CanBeSavedToNBT;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagFloat;

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

	/**
	 * The tag's name is also the username
	 */
	@Override
	public void writeToNBT(TagCompound tag) {
		TagCompound inv = new TagCompound("Inventory");
		inventory.writeToNBT(inv);
		tag.setTag(inv);
		
		tag.setTag(new TagFloat("PosX", posx));
		tag.setTag(new TagFloat("PosY", posy));
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		inventory.readFromNBT(tag.getCompound("Inventory"));
		username = tag.getName();
		
		posx = tag.getFloat("PosX");
		posy = tag.getFloat("PosY");
	}
	
}
