package projectmp.common.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import projectmp.common.entity.Entity;
import projectmp.common.world.World;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.stream.NbtInputStream;
import com.evilco.mc.nbt.stream.NbtOutputStream;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;
import com.evilco.mc.nbt.tag.TagList;
import com.evilco.mc.nbt.tag.TagLong;

public class WorldNBTIO {

	public static final int SAVE_FORMAT_VERSION = 1;
	
	public static byte[] encode(World world) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		NbtOutputStream nbtStream = new NbtOutputStream(byteStream);
		
		TagCompound compound = new TagCompound("World");
		
		// add save format version and other important data
		compound.setTag(new TagInteger("SaveFormatVersion", SAVE_FORMAT_VERSION));
		compound.setTag(new TagInteger("WorldWidth", world.sizex));
		compound.setTag(new TagInteger("WorldHeight", world.sizey));
		compound.setTag(new TagInteger("WorldTime", world.worldTime.totalTicks));
		compound.setTag(new TagLong("WorldSeed", world.seed));
		
		// chunks
		TagList chunksList = new TagList("Chunks");
		for(int x = 0; x < world.getWidthInChunks(); x++){
			for(int y = 0; y < world.getHeightInChunks(); y++){
				TagCompound chunkTag = new TagCompound("Chunk_" + x + "," + y);
				
				world.getChunk(x, y).writeToNBT(chunkTag);
				
				chunksList.addTag(chunkTag);
			}
		}
		compound.setTag(chunksList);
		
		TagList entitiesList = new TagList("Entities");
		
		compound.setTag(entitiesList);
		
		nbtStream.write(compound);
		nbtStream.close();
		return byteStream.toByteArray();
	}

	public static World decode(World world, byte[] bytes) throws IOException, TagNotFoundException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		NbtInputStream nbtStream = new NbtInputStream(byteStream);
		TagCompound tag = (TagCompound) nbtStream.readTag();
		
		world.sizex = tag.getInteger("WorldWidth");
		world.sizey = tag.getInteger("WorldHeight");
		world.worldTime.setTotalTime(tag.getInteger("WorldTime"));
		world.seed = tag.getInteger("WorldSeed");
		
		world.prepare();
		
		// set world
		for(int x = 0; x < world.getWidthInChunks(); x++){
			for(int y = 0; y < world.getHeightInChunks(); y++){
				TagCompound chunkTag = tag.getCompound("Chunk_" + x + "," + y);
				
				world.getChunk(x, y).readFromNBT(chunkTag);
			}
		}
		
		// entities
		
		nbtStream.close();
		return world;
	}

}
