package projectmp.common.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import projectmp.common.Main;
import projectmp.common.entity.Entity;
import projectmp.common.registry.GameRegistry;
import projectmp.common.world.World;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.stream.NbtInputStream;
import com.evilco.mc.nbt.stream.NbtOutputStream;
import com.evilco.mc.nbt.tag.ITag;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;
import com.evilco.mc.nbt.tag.TagList;
import com.evilco.mc.nbt.tag.TagLong;
import com.evilco.mc.nbt.tag.TagString;

public final class WorldNBTIO {

	private WorldNBTIO(){}
	
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
		TagCompound chunksList = new TagCompound("Chunks");
		for(int x = 0; x < world.getWidthInChunks(); x++){
			for(int y = 0; y < world.getHeightInChunks(); y++){
				TagCompound chunkTag = new TagCompound("Chunk_" + x + "," + y);
				
				world.getChunk(x, y).writeToNBT(chunkTag);
				
				chunksList.setTag(chunkTag);
			}
		}
		compound.setTag(chunksList);
		
		TagList entitiesList = new TagList("Entities");
		
		for(int i = 0; i < world.entities.size; i++){
			Entity e = world.entities.get(i);
			
			TagCompound entTag = new TagCompound("Entity");
			
			entTag.setTag(new TagString("EntityType", GameRegistry.getEntityRegistry().getKey(e.getClass())));
			e.writeToNBT(entTag);
			
			entitiesList.addTag(entTag);
		}
		
		compound.setTag(entitiesList);
		
		nbtStream.write(compound);
		nbtStream.close();
		return byteStream.toByteArray();
	}

	public static World decode(World world, byte[] bytes) throws IOException, TagNotFoundException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		NbtInputStream nbtStream = new NbtInputStream(byteStream);
		TagCompound tag = (TagCompound) nbtStream.readTag();
		int saveFormat = tag.getInteger("SaveFormatVersion");
		
		world.sizex = tag.getInteger("WorldWidth");
		world.sizey = tag.getInteger("WorldHeight");
		world.worldTime.setTotalTime(tag.getInteger("WorldTime"));
		world.seed = tag.getLong("WorldSeed");
		
		world.prepare();
		
		// set world
		TagCompound chunkCompound = tag.getCompound("Chunks");
		for(int x = 0; x < world.getWidthInChunks(); x++){
			for(int y = 0; y < world.getHeightInChunks(); y++){
				TagCompound chunkTag = chunkCompound.getCompound("Chunk_" + x + "," + y);
				
				world.getChunk(x, y).readFromNBT(chunkTag);
			}
		}
		
		// entities
		List<TagCompound> entitiesList = tag.getList("Entities", TagCompound.class);
		for(TagCompound comp : entitiesList){
			Entity e = null;
			try {
				e = GameRegistry.getEntityRegistry().getValue(comp.getString("EntityType")).newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
				Main.logger.error("Failed to re-create entity of type " + comp.getString("EntityType"), ex);
			}
			
			if(e != null){
				e.world = world;
				e.readFromNBT(comp);
				
				world.entities.add(e);
			}
			
		}
		
		nbtStream.close();
		return world;
	}

}
