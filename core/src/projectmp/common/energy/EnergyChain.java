package projectmp.common.energy;

import projectmp.common.block.Block;
import projectmp.common.util.Coordinate;
import projectmp.common.world.World;

public class EnergyChain {

	public static final Coordinate[] OFFSETS = new Coordinate[] { new Coordinate(0, -1),
			new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(-1, 0) };

	public static int doSpreadPower(int energy, int startX, int startY, World world) {
		int remainingEnergy = 0;
		int[] energyEach = new int[]{-1, -1, -1, -1};

		energyEach = rationPower(energy, startX, startY, startX, startY, world, energyEach);

		// spread
		for (int i = 0; i < OFFSETS.length; i++) {
			if (energyEach[i] > 0) {
				traverse(energyEach[i], startX + OFFSETS[i].getX(), startY + OFFSETS[i].getY(),
						startX, startY, world);
			}
		}

		return remainingEnergy;
	}

	private static int traverse(int energy, int startX, int startY, int bannedX, int bannedY,
			World world) {
		int currentX = startX;
		int currentY = startY;
		int prevX = currentX;
		int prevY = currentY;
		int remainingEnergy = energy;
		int[] energyArray = new int[]{-1, -1, -1, -1};

		while (true) {
			Block currentBlock = world.getBlock(currentX, currentY);

			if (currentBlock instanceof IPowerHandler) {
				IPowerHandler phandler = (IPowerHandler) currentBlock;

				// give power to current block
				int energyUsed = phandler.receiveEnergy(remainingEnergy, false);
				remainingEnergy -= energyUsed;
				
				if(remainingEnergy <= 0) break;

				// find next block
				int futureX = currentX;
				int futureY = currentY;
				neg1Array(energyArray);
				rationPower(energy, currentX, currentY, prevX, prevY, world, energyArray);
				
				for (int i = 0; i < OFFSETS.length; i++) {
					if(energyArray[i] != -1){
						if(futureX == currentX && futureY == currentY){
							// first find
							futureX = currentX + OFFSETS[i].getX();
							futureY = currentY + OFFSETS[i].getY();
							
							// set new remaining energy to what's leftover after rationing
							remainingEnergy = energyArray[i];
						}else{
							// not first find
							// recurse at new area
							traverse(energyArray[i], currentX + OFFSETS[i].getX(), currentY + OFFSETS[i].getY(), currentX, currentY, world);
						}
					}
				}

				if (currentX == futureX && currentY == futureY) {
					// found nothing, end the while loop
					break;
				} else {
					// found something, continue
					prevX = currentX;
					prevY = currentY;
					currentX = futureX;
					currentY = futureY;
				}
			}
		}

		return remainingEnergy;
	}

	private static int[] rationPower(int energy, int startX, int startY, int bannedX, int bannedY, World world, int[] array) {
		int[] energyEach = array;
		int energyRemainder = 0;
		int connectionsFound = 0;

		// find connections
		for (int i = 0; i < OFFSETS.length; i++) {
			if (world.getBlock(startX + OFFSETS[i].getX(), startY + OFFSETS[i].getY()) instanceof IPowerHandler) {
				if(startX + OFFSETS[i].getX() == bannedX && startY + OFFSETS[i].getY() == bannedY) continue;
				
				connectionsFound++;
				energyEach[i] = 0;
			}
		}

		// ration energy to each connection
		for (int i = 0; i < OFFSETS.length; i++) {
			if (energyEach[i] > -1) {
				IPowerHandler connector = (IPowerHandler) world.getBlock(
						startX + OFFSETS[i].getX(), startY + OFFSETS[i].getY());
				energyEach[i] = Math.min(connector.getMaxReceive(), energy / connectionsFound);
			}
		}

		// calculate remainder and give to the first connection
		int totalEnergyRationedSoFar = 0;
		for (int i = 0; i < energyEach.length; i++) {
			if (energyEach[i] > 0) {
				totalEnergyRationedSoFar += energyEach[i];
			}
		}
		energyRemainder = energy - totalEnergyRationedSoFar; // remainder
		for (int i = 0; i < energyEach.length; i++) {
			if (energyEach[i] > 0) {
				energyEach[i] += energyRemainder;
				break;
			}
		}

		return energyEach;
	}
	
	private static void neg1Array(int[] array){
		for(int i = 0; i < array.length; i++){
			array[i] = -1;
		}
	}

}
