package projectmp.common.entity;

import projectmp.client.WorldRenderer;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.io.Unsaveable;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.tileentity.HasInventory;
import projectmp.common.util.NBTUtils;
import projectmp.common.util.Utils;
import projectmp.common.world.World;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagString;

public class EntityPlayer extends EntityLiving implements HasInventory, ILoadsChunk, Unsaveable {

	public String username = "UNKNOWN PLAYER NAME RAWR";
	private InventoryPlayer inventory;

	public EntityPlayer() {
		super();
	}

	public EntityPlayer(World w, float posx, float posy) {
		super(w, posx, posy);
	}

	@Override
	public void prepare() {
		this.maxspeed = 15f;
		this.accspeed = maxspeed * 5f;
		this.sizex = 2f - (4 * World.tilepartx);
		this.sizey = 2f - (3 * World.tileparty);
		
		this.maxhealth = 100;
		this.health = maxhealth;
		
		inventory = new InventoryPlayer(uuid);
	}

	@Override
	public void render(WorldRenderer renderer) {
		this.drawTextureCenteredWithFacing(renderer, AssetRegistry.getTexture("player"));
		
		renderer.startBypassing();
		this.drawTextureCenteredWithFacing(renderer, AssetRegistry.getTexture("player_eyes"));
		renderer.stopBypassing();
	}

	@Override
	public void writeToNBT(TagCompound tag) {
		super.writeToNBT(tag);

		tag.setTag(new TagString("Username", username));
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		super.readFromNBT(tag);

		username = NBTUtils.getStringWithDef(tag, "Username", this.username);
	}

	@Override
	public InventoryPlayer getInventoryObject() {
		return inventory;
	}
	
	public void setInventory(InventoryPlayer p){
		inventory = p;
	}

	@Override
	public void setInventoryObject(Inventory inv) {
		inventory = (InventoryPlayer) inv;
	}
	
	@Override
	public void damage(int dmg){
		super.damage(dmg);
	}
	
	@Override
	/**
	 * This method in EntityPlayer always returns false (you cannot remove a player entity via conventional methods, there is special handling for death)
	 */
	public boolean isMarkedForRemoval(){
		return false;
	}

}
