package projectmp.common.tileentity;

import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;

import projectmp.common.energy.EnergyStorage;
import projectmp.common.energy.IPowerHandler;


public abstract class TileEntityPowerHandler extends TileEntity implements IPowerHandler{

	private EnergyStorage energyStorage = new EnergyStorage(0, Short.MAX_VALUE, 16, 16);
	
	public TileEntityPowerHandler(int x, int y){
		super(x, y);
	}
	
	public TileEntityPowerHandler(){
		super();
	}
	
	@Override
	public void writeToNBT(TagCompound tag) {
		energyStorage.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		energyStorage.readFromNBT(tag);
	}
	
	@Override
	public int receiveEnergy(int requestedAmount, boolean sim) {
		return energyStorage.receiveEnergy(requestedAmount, sim);
	}

	@Override
	public int extractEnergy(int requestedAmount, boolean sim) {
		return energyStorage.extractEnergy(requestedAmount, sim);
	}

	@Override
	public int getMaxCapacity() {
		return energyStorage.getCapacity();
	}

	@Override
	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxSend() {
		return energyStorage.getMaxSend();
	}

	@Override
	public int getMaxReceive() {
		return energyStorage.getMaxReceive();
	}

	@Override
	public void setMaxSend(int max) {
		energyStorage.setMaxSend(max);
	}

	@Override
	public void setMaxReceive(int max) {
		energyStorage.setMaxReceive(max);
	}

	@Override
	public void setMaxTransfer(int max) {
		energyStorage.setMaxTransfer(max);
	}

	@Override
	public void setMaxCapacity(int max) {
		energyStorage.setCapacity(max);
	}

	@Override
	public void setEnergyStored(int energy) {
		energyStorage.setEnergy(energy);
	}

}
