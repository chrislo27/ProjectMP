package projectmp.common.util;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.ITag;
import com.evilco.mc.nbt.tag.TagCompound;


public final class NBTUtils {

	private NBTUtils(){}
	
	public static int getIntWithDef(TagCompound tag, String key, int def){
		try {
			return tag.getInteger(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}
	
	public static short getShortWithDef(TagCompound tag, String key, short def){
		try {
			return tag.getShort(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}
	
	public static byte getByteWithDef(TagCompound tag, String key, byte def){
		try {
			return tag.getByte(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}
	
	public static long getLongWithDef(TagCompound tag, String key, long def){
		try {
			return tag.getLong(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}

	public static double getDoubleWithDef(TagCompound tag, String key, double def){
		try {
			return tag.getDouble(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}
	
	public static float getFloatWithDef(TagCompound tag, String key, float def){
		try {
			return tag.getFloat(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}
	
	public static String getStringWithDef(TagCompound tag, String key, String def){
		try {
			return tag.getString(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}
	
	public static int[] getIntArrayWithDef(TagCompound tag, String key, int[] def){
		try {
			return tag.getIntegerArray(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}
	
	public static byte[] getByteArrayWithDef(TagCompound tag, String key, byte[] def){
		try {
			return tag.getByteArray(key);
		} catch (UnexpectedTagTypeException | TagNotFoundException e) {
			return def;
		}
	}
	
}
