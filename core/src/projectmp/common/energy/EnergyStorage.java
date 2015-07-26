package projectmp.common.energy;

import projectmp.common.io.CanBeSavedToNBT;
import projectmp.common.util.NBTUtils;

import com.badlogic.gdx.math.MathUtils;
import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagInteger;

/**
 * Stores energy along with data for max receive, max send, and capacity.
 * 
 *
 */
public class EnergyStorage implements CanBeSavedToNBT{

	int energy = 0;
	int capacity = 1;
	int maxSend, maxReceive;

	public EnergyStorage(int startingEnergy, int capacity, int maxSend, int maxGet) {
		this.maxSend = maxSend;
		maxReceive = maxGet;
		energy = startingEnergy;
		this.capacity = capacity;
	}

	/**
	 * 
	 * @param amount
	 * @param simulate if it should just return how much it could store rather than actually storing
	 * @return amount it can take/took
	 */
	public int receiveEnergy(int amount, boolean simulate) {
		// it can take enough to fill the capacity or the max receiving amount or the amount if it's small enough
		int canTake = Math.min(capacity - energy, Math.min(this.maxReceive, amount));

		if (!simulate) energy += canTake;

		return canTake;
	}

	public int extractEnergy(int amount, boolean simulate) {
		// it can give either all its energy, its max sending amount, or the requested amount if it's small enough
		int canGive = Math.min(energy, Math.min(this.maxSend, amount));

		if (!simulate) energy -= canGive;

		return canGive;
	}

	public int getEnergyStored() {
		return energy;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getMaxSend() {
		return maxSend;
	}
	
	public void setMaxTransfer(int transfer){
		setMaxReceive(transfer);
		setMaxSend(transfer);
	}

	public void setEnergy(int energy) {
		this.energy = MathUtils.clamp(energy, 0, capacity);
	}

	public int getMaxReceive() {
		return maxReceive;
	}

	public void setMaxReceive(int maxReceive) {
		this.maxReceive = maxReceive;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setMaxSend(int maxSend) {
		this.maxSend = maxSend;
	}

	@Override
	public void writeToNBT(TagCompound tag) {
		tag.setTag(new TagInteger("Energy", energy));
	}

	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException,
			UnexpectedTagTypeException {
		setEnergy(NBTUtils.getIntWithDef(tag, "Energy", 0));
	}

}
