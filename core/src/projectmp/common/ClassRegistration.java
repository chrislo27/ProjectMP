package projectmp.common;

import projectmp.common.packet.Packet;
import projectmp.common.packet.Packet0Handshake;
import projectmp.common.packet.Packet1Chunk;
import projectmp.common.packet.Packet2BlockUpdate;
import projectmp.common.packet.Packet3Entities;
import projectmp.common.packet.Packet4PlayerUpdate;
import projectmp.common.packet.Packet5;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;


public class ClassRegistration {

	public static void registerClasses(Kryo kryo){
		kryo.register(Array.class);
		registerPackets(kryo);
	}
	
	private static void registerPackets(Kryo kryo){
		kryo.register(Packet.class);
		kryo.register(Packet0Handshake.class);
		kryo.register(Packet1Chunk.class);
		kryo.register(Packet2BlockUpdate.class);
		kryo.register(Packet3Entities.class);
		kryo.register(Packet4PlayerUpdate.class);
		kryo.register(Packet5.class);
	}
	
}
