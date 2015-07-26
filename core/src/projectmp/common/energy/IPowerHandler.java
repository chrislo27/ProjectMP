package projectmp.common.energy;

/**
 * Has send/receive and getters for energy storage. Used for machines, generators, and storage devices (essentially anything not a cable)
 * 
 *
 */
public interface IPowerHandler{
	
	public int receiveEnergy(int requestedAmount, boolean sim);
	
	public int extractEnergy(int requestedAmount, boolean sim);
	
	public int getMaxCapacity();
	
	public int getEnergyStored();
	
	public int getMaxSend();
	
	public int getMaxReceive();
	
}
