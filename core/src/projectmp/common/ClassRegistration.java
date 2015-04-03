package projectmp.common;

import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.entity.EntitySquare;
import projectmp.common.packet.Packet;
import projectmp.common.packet.Packet0Handshake;
import projectmp.common.packet.Packet10EndChunkTransfer;
import projectmp.common.packet.Packet1Chunk;
import projectmp.common.packet.Packet2BlockUpdate;
import projectmp.common.packet.Packet3Entities;
import projectmp.common.packet.Packet4PositionUpdate;
import projectmp.common.packet.Packet5PlayerPosUpdate;
import projectmp.common.packet.Packet7NewEntity;
import projectmp.common.packet.Packet8RemoveEntity;
import projectmp.common.packet.Packet6;
import projectmp.common.packet.Packet9BeginChunkTransfer;

import com.esotericsoftware.kryo.Kryo;


public class ClassRegistration {

	public static void registerClasses(Kryo kryo){
		kryo.register(Entity[].class);
		kryo.register(String[][].class);
		kryo.register(int[][].class);
		kryo.register(String[].class);
		kryo.register(int[].class);
		kryo.register(long[].class);
		kryo.register(float[].class);
		registerPackets(kryo);
		registerEntities(kryo);
	}
	
	private static void registerPackets(Kryo kryo){
		kryo.register(Packet.class);
		kryo.register(Packet0Handshake.class);
		kryo.register(Packet1Chunk.class);
		kryo.register(Packet2BlockUpdate.class);
		kryo.register(Packet3Entities.class);
		kryo.register(Packet4PositionUpdate.class);
		kryo.register(Packet5PlayerPosUpdate.class);
		kryo.register(Packet6.class);
		kryo.register(Packet7NewEntity.class);
		kryo.register(Packet8RemoveEntity.class);
		kryo.register(Packet9BeginChunkTransfer.class);
		kryo.register(Packet10EndChunkTransfer.class);
	}
	
	private static void registerEntities(Kryo kryo){
		kryo.register(Entity.class);
		kryo.register(EntityPlayer.class);
		kryo.register(EntitySquare.class);
	}
	
}
