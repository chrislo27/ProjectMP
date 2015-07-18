package projectmp.common.entity;

import projectmp.client.WorldRenderer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.world.World;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

public class EntityItem extends Entity {

	private ItemStack itemStack;

	public EntityItem() {
		super();
	}

	public EntityItem(World w, float posx, float posy, ItemStack stack) {
		super(w, posx, posy);

		setItemStack(stack);
	}

	@Override
	public void prepare() {
		sizex = 0.5f;
		sizey = 0.5f;
	}

	@Override
	public void render(WorldRenderer renderer) {
		if (itemStack == null || itemStack.isNothing()) return;

		itemStack.getItem().render(renderer,
				renderer.convertWorldX(visualX + (sizex / 2)) - World.tilesizex / 2f,
				renderer.convertWorldY((visualY + (sizey)), 0), World.tilesizex * sizex,
				World.tilesizey * sizey, itemStack);
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();

		if (itemStack == null || itemStack.isNothing()) {
			markForRemoval();
			return;
		}
	}

	@Override
	public void writeToNBT(TagCompound tag) {
		super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		super.readFromNBT(tag);
	}

	public EntityItem setItemStack(ItemStack stack) {
		itemStack = stack;

		return this;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

}
