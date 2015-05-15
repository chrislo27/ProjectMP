package projectmp.common.nbt;

import com.evilco.mc.nbt.tag.TagCompound;

/**
 * Having the weirdest name, this interface has write and read methods to/from a NBTTagCompound.
 * 
 *
 */
public interface NBTIOAble {

	/**
	 * 
	 * @param tag TagCompound for the object only
	 * @return
	 */
	public TagCompound writeToNBT(TagCompound tag);
	
	public void readFromNBT(TagCompound tag);
	
}
