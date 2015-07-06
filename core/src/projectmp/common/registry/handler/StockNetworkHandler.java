package projectmp.common.registry.handler;

import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityLiving;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.Inventory;
import projectmp.common.inventory.InventoryChessboard;
import projectmp.common.inventory.InventoryPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.packet.Packet;
import projectmp.common.packet.PacketBeginChunkTransfer;
import projectmp.common.packet.PacketBlockActivate;
import projectmp.common.packet.PacketBlockUpdate;
import projectmp.common.packet.PacketEndChunkTransfer;
import projectmp.common.packet.PacketEntities;
import projectmp.common.packet.PacketItemUse;
import projectmp.common.packet.PacketNewEntity;
import projectmp.common.packet.PacketPlayerPosUpdate;
import projectmp.common.packet.PacketPositionUpdate;
import projectmp.common.packet.PacketRemoveEntity;
import projectmp.common.packet.PacketSendChunk;
import projectmp.common.packet.PacketSendTileEntity;
import projectmp.common.packet.PacketSlotChanged;
import projectmp.common.packet.PacketSwapSlot;
import projectmp.common.packet.PacketTimeUpdate;
import projectmp.common.packet.PacketWeather;
import projectmp.common.packet.handshake.PacketHandshake;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.tileentity.TileEntityChessboard;

import com.esotericsoftware.kryo.Kryo;


public class StockNetworkHandler implements INetworkHandler{

	@Override
	public void registerClasses(Kryo kryo) {
		registerPackets(kryo);
		registerEntities(kryo);
		registerTileEntities(kryo);
		registerInventories(kryo);
		kryo.register(Entity[].class);
		kryo.register(String[][].class);
		kryo.register(int[][].class);
		kryo.register(String[].class);
		kryo.register(int[].class);
		kryo.register(long[].class);
		kryo.register(float[].class);
		kryo.register(ItemStack.class);
		kryo.register(ItemStack[].class);
	}

	private void registerPackets(Kryo kryo) {
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
		kryo.register(PacketWeather.class);
		kryo.register(PacketSendTileEntity.class);
		kryo.register(PacketSlotChanged.class);
		kryo.register(PacketSwapSlot.class);
		kryo.register(PacketBlockActivate.class);
		kryo.register(PacketItemUse.class);
	}

	private void registerEntities(Kryo kryo) {
		kryo.register(Entity.class);
		kryo.register(EntityLiving.class);
		kryo.register(EntityPlayer.class);
	}

	private void registerTileEntities(Kryo kryo) {
		kryo.register(TileEntity.class);
		kryo.register(TileEntity[].class);
		kryo.register(TileEntity[][].class);
		kryo.register(TileEntityChessboard.class);
	}
	
	private void registerInventories(Kryo kryo){
		kryo.register(Inventory.class);
		kryo.register(InventoryPlayer.class);
		kryo.register(InventoryChessboard.class);
	}

}
