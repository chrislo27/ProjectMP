package projectmp.common;

import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityLiving;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.ItemStack;
import projectmp.common.packet.Packet;
import projectmp.common.packet.PacketBeginChunkTransfer;
import projectmp.common.packet.PacketBlockUpdate;
import projectmp.common.packet.PacketEndChunkTransfer;
import projectmp.common.packet.PacketEntities;
import projectmp.common.packet.PacketHandshake;
import projectmp.common.packet.PacketNewEntity;
import projectmp.common.packet.PacketPlayerInventory;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.packet.PacketPositionUpdate;
import projectmp.common.packet.PacketRemoveEntity;
import projectmp.common.packet.PacketSendChunk;
import projectmp.common.packet.PacketSendInv;
import projectmp.common.packet.PacketTimeUpdate;

import com.esotericsoftware.kryo.Kryo;


public class ClassRegistration {

	public static void registerClasses(Kryo kryo){
		registerPackets(kryo);
		registerEntities(kryo);
		kryo.register(Entity[].class);
		kryo.register(String[][].class);
		kryo.register(int[][].class);
		kryo.register(String[].class);
		kryo.register(int[].class);
		kryo.register(long[].class);
		kryo.register(float[].class);
		kryo.register(Inventory.class);
		kryo.register(ItemStack.class);
		kryo.register(ItemStack[].class);
	}
	
	private static void registerPackets(Kryo kryo){
		kryo.register(Packet.class);
		kryo.register(PacketHandshake.class);
		kryo.register(PacketSendChunk.class);
		kryo.register(PacketBlockUpdate.class);
		kryo.register(PacketEntities.class);
		kryo.register(PacketPositionUpdate.class);
		kryo.register(PacketPlayerPosUpdate.class);
		kryo.register(PacketNewEntity.class);
		kryo.register(PacketRemoveEntity.class);
		kryo.register(PacketBeginChunkTransfer.class);
		kryo.register(PacketEndChunkTransfer.class);
		kryo.register(PacketTimeUpdate.class);
		kryo.register(PacketSendInv.class);
		kryo.register(PacketPlayerInventory.class);
	}
	
	private static void registerEntities(Kryo kryo){
		kryo.register(Entity.class);
		kryo.register(EntityLiving.class);
		kryo.register(EntityPlayer.class);
	}
	
}
