package projectmp.common.block;

import projectmp.common.energy.IPowerHandler;


public class BlockCable extends Block implements IPowerHandler{

	private int transferRate = 16;
	
	public BlockCable(String unlocalName) {
		super(unlocalName);
	}

	@Override
	public int receiveEnergy(int requestedAmount, boolean sim) {
		return 0;
	}

	@Override
	public int extractEnergy(int requestedAmount, boolean sim) {
		return 0;
	}

	@Override
	public int getMaxCapacity() {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return 0;
	}

	@Override
	public int getMaxSend() {
		return transferRate;
	}

	@Override
	public int getMaxReceive() {
		return transferRate;
	}

	@Override
	public void setMaxSend(int max) {
		setMaxTransfer(max);
	}

	@Override
	public void setMaxReceive(int max) {
		setMaxTransfer(max);
	}

	@Override
	public void setMaxTransfer(int max) {
		transferRate = max;
	}

	@Override
	public void setMaxCapacity(int max) {
	}

	@Override
	public void setEnergyStored(int energy) {
	}

}
