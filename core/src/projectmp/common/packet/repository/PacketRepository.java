package projectmp.common.packet.repository;

import projectmp.common.packet.PacketBlockActivate;
import projectmp.common.packet.PacketBlockUpdate;
import projectmp.common.packet.PacketBreakingProgress;
import projectmp.common.packet.PacketGuiState;
import projectmp.common.packet.PacketItemUse;
import projectmp.common.packet.PacketNewEntity;
import projectmp.common.packet.PacketPositionUpdate;
import projectmp.common.packet.PacketRemoveEntity;
import projectmp.common.packet.PacketSendInventory;
import projectmp.common.packet.PacketSendTileEntity;
import projectmp.common.packet.PacketSlotChanged;
import projectmp.common.packet.PacketSwapSlot;
import projectmp.common.packet.PacketTimeUpdate;
import projectmp.common.packet.PacketUpdateCursor;
import projectmp.common.packet.PacketUpdateHealth;
import projectmp.common.packet.PacketWeather;


public final class PacketRepository {

	private static PacketRepository instance;

	private PacketRepository() {
	}

	public static synchronized PacketRepository instance() {
		if (instance == null) {
			instance = new PacketRepository();
			instance.loadResources();
		}
		return instance;
	}

	public PacketNewEntity newEntity = new PacketNewEntity();
	public PacketRemoveEntity removeEntity = new PacketRemoveEntity();
	
	public PacketSendTileEntity sendTileEntity = new PacketSendTileEntity();
	public PacketPositionUpdate positionUpdate = new PacketPositionUpdate();
	public PacketBlockUpdate blockUpdate = new PacketBlockUpdate();
	public PacketTimeUpdate timeUpdate = new PacketTimeUpdate();
	public PacketUpdateHealth updateHealth = new PacketUpdateHealth();
	public PacketWeather weatherUpdate = new PacketWeather();
	public PacketBreakingProgress breakingProgress = new PacketBreakingProgress();
	public PacketSendInventory sendInv = new PacketSendInventory();
	
	public PacketGuiState guiState = new PacketGuiState();
	public PacketSwapSlot swapSlot = new PacketSwapSlot();
	public PacketSlotChanged slotChanged = new PacketSlotChanged();
	
	public PacketBlockActivate blockActivate = new PacketBlockActivate();
	public PacketItemUse itemUse = new PacketItemUse();
	public PacketUpdateCursor updateCursor = new PacketUpdateCursor();
	
	private void loadResources() {

	}
	
}
