package projectmp.common.packet.repository;

import projectmp.common.packet.*;


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
	
	public PacketGuiState guiState = new PacketGuiState();
	public PacketSwapSlot swapSlot = new PacketSwapSlot();
	public PacketSlotChanged slotChanged = new PacketSlotChanged();
	
	private void loadResources() {

	}
	
}
