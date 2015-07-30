package projectmp.common.entity;

import java.util.List;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Utils;
import projectmp.common.world.World;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

public class EntityItem extends Entity {

	private ItemStack itemStack;

	public EntityItem() {
		super();
		prepare();
	}

	public EntityItem(World w, float posx, float posy, ItemStack stack) {
		super(w, posx, posy);
		prepare();
		
		setItemStack(stack);
	}

	public void prepare() {
		sizex = 0.5f;
		sizey = 0.5f;
	}

	@Override
	public void render(WorldRenderer renderer) {
		if (itemStack == null || itemStack.isNothing()) return;

		itemStack.getItem().render(
				renderer,
				renderer.convertWorldX(visualX + (sizex / 2)) - World.tilesizex / 2f,
				renderer.convertWorldY((visualY + (sizey)), 0)
						+ MathHelper.clampNumberFromTime(System.currentTimeMillis()
								+ (timeInstantiated * 750), 2.5f) * 8, World.tilesizex * sizex,
				World.tilesizey * sizey, itemStack);
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();

		if (itemStack == null || itemStack.isNothing()) {
			markForRemoval();
			return;
		}

		if (world.isServer) {
			List<Entity> nearby = world.getQuadArea(this);

			for (int i = nearby.size() - 1; i >= 0; i--) {
				if (!(nearby.get(i) instanceof EntityPlayer)) continue;

				EntityPlayer player = (EntityPlayer) nearby.get(i);
				float blockRange = 2f;

				if (MathHelper.intersects(x - blockRange, y - blockRange, sizex + blockRange * 2,
						sizey + blockRange * 2, player.x, player.y, player.sizex, player.sizey)) {
					float distance = MathHelper.distanceSquared(x, y, player.x, player.y);
					float maxspeed = 16f;
					
					if (Math.abs(velox) < maxspeed) velox += (((player.x - x) / 2) * distance);
					if (Math.abs(veloy) < maxspeed) veloy += (((player.y - y) / 2) * distance);
				}

				if (!this.intersectingOther(player)) continue;

				player.getInventoryObject().addStack(itemStack);

				world.main.serverLogic.updateClientsOfTotalInventoryChange("playerInv",
						Utils.unpackLongUpper(player.uuid), Utils.unpackLongLower(player.uuid));

				if (itemStack.isNothing()) {
					markForRemoval();
					return;
				}
			}
		}

	}

	@Override
	public void writeToNBT(TagCompound tag) {
		super.writeToNBT(tag);

		TagCompound is = new TagCompound("Item");
		itemStack.writeToNBT(is);
		tag.setTag(is);
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		super.readFromNBT(tag);

		itemStack = new ItemStack(null, 0);
		itemStack.readFromNBT(tag.getCompound("Item"));
	}

	public EntityItem setItemStack(ItemStack stack) {
		itemStack = stack;

		return this;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

}
