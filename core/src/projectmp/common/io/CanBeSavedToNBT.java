package projectmp.common.io;

import com.evilco.mc.nbt.tag.TagCompound;

/**
 * Indicates the class can read and write to an NBT compound tag
 * 
 *
 */
public interface CanBeSavedToNBT {

	/**
	 * 
	 * @param tag TagCompound for the object only
	 */
	public void writeToNBT(TagCompound tag);
	
	public void readFromNBT(TagCompound tag);
	
}
