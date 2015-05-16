package projectmp.common.tileentity;

import com.evilco.mc.nbt.tag.TagCompound;

import projectmp.common.nbt.NBTIOAble;


public class TileEntity implements NBTIOAble{

	@Override
	public TagCompound writeToNBT(TagCompound tag) {
		return tag;
	}

	@Override
	public void readFromNBT(TagCompound tag) {
	}

}
