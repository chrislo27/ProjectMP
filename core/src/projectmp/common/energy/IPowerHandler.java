package projectmp.common.energy;

/**
 * Has send/receive and getters for energy storage. Used for machines, generators, and storage devices (essentially anything not a cable)
 * 
 *
 */
public interface IPowerHandler{

	public boolean canConnectTo(int blockFace);
	
	public int receiveEnergy(int blockFace, int requestedAmount, boolean sim);
	
	public int extractEnergy(int blockFace, int requestedAmount, boolean sim);
	
	public int getMaxCapacity(int blockFace);
	
	public int getEnergyStored(int blockFace);
	
	public int getMaxSend(int blockFace);
	
	public int getMaxReceive(int blockFace);
	
}
