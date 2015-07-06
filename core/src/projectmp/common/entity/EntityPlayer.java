package projectmp.common.entity;

import projectmp.client.WorldRenderer;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.tileentity.HasInventory;
import projectmp.common.util.AssetMap;
import projectmp.common.util.NBTUtils;
import projectmp.common.util.Utils;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.Texture;
import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagString;

public class EntityPlayer extends EntityLiving implements HasInventory, ILoadsChunk {

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
		this.hasEntityCollision = true;
		
		inventory = new InventoryPlayer("playerInv", Utils.unpackLongUpper(uuid), Utils.unpackLongLower(uuid));
	}

	@Override
	public void render(WorldRenderer renderer) {
		this.drawTextureCenteredWithFacing(renderer, AssetRegistry.getTexture("player"));
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
	public Inventory getInventoryObject() {
		return inventory;
	}

}
