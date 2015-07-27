package projectmp.common.energy;

import java.util.HashMap;

import projectmp.common.block.Block;
import projectmp.common.block.BlockPowerHandler;
import projectmp.common.util.Coordinate;
import projectmp.common.util.Utils;
import projectmp.common.world.World;

import com.badlogic.gdx.utils.LongMap;

public class EnergyChain {

	public static final Coordinate[] OFFSETS = new Coordinate[] { new Coordinate(0, -1),
			new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(-1, 0) };

	public static int doSpreadPower(int energy, int startX, int startY, World world) {
		int remainingEnergy = 0;
		int[] energyEach = new int[] { -1, -1, -1, -1 };
		
		LongMap traversedMap = TraversedMapPool.get();

		energyEach = rationPower(energy, startX, startY, startX, startY, world, energyEach, traversedMap);
		
		// spread
		for (int i = 0; i < OFFSETS.length; i++) {
			if (energyEach[i] > 0) {
				remainingEnergy += traverse(energyEach[i], startX + OFFSETS[i].getX(), startY
						+ OFFSETS[i].getY(), startX, startY, world, traversedMap);
			}
		}

		return remainingEnergy;
	}

	/**
	 * 
	 * @param energy
	 * @param startX
	 * @param startY
	 * @param bannedX
	 * @param bannedY
	 * @param world
	 * @return remaining energy that wasn't given away
	 */
	private static int traverse(int energy, int startX, int startY, int bannedX, int bannedY,
			World world, LongMap traversed) {
		int currentX = startX;
		int currentY = startY;
		int prevX = bannedX;
		int prevY = bannedY;
		int remainingEnergy = energy;
		int[] energyArray = new int[] { -1, -1, -1, -1 };
		
		traversed.put(Utils.packLong(bannedX, bannedY), true);

		while (true) {
			Block currentBlock = world.getBlock(currentX, currentY);

			if (currentBlock instanceof BlockPowerHandler || currentBlock instanceof IPowerHandler) {
				IPowerHandler phandler = null;

				if (currentBlock instanceof BlockPowerHandler) {
					phandler = (IPowerHandler) (((BlockPowerHandler) currentBlock).getPowerHandler(
							world, currentX, currentY));
				} else if (currentBlock instanceof IPowerHandler) {
					phandler = (IPowerHandler) currentBlock;
				}

				// give power to current block
				int energyUsed = phandler.receiveEnergy(remainingEnergy, false);
				remainingEnergy -= energyUsed;

				if (remainingEnergy <= 0) break;

				// find next block
				int futureX = currentX;
				int futureY = currentY;
				neg1Array(energyArray);
				rationPower(remainingEnergy, currentX, currentY, prevX, prevY, world, energyArray, traversed);
				//Main.logger.debug(currentX + ", " + currentY + "|" + Arrays.toString(energyArray));

				for (int i = 0; i < OFFSETS.length; i++) {
					if (energyArray[i] != -1) {
						if (futureX == currentX && futureY == currentY) {
							// first find
							futureX = currentX + OFFSETS[i].getX();
							futureY = currentY + OFFSETS[i].getY();

							// set new remaining energy to what's leftover after rationing
							remainingEnergy = energyArray[i];
						} else {
							// not first find
							// recurse at new area
							remainingEnergy += traverse(energyArray[i],
									currentX + OFFSETS[i].getX(), currentY + OFFSETS[i].getY(),
									currentX, currentY, world, TraversedMapPool.get());
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
			} else {
				break;
			}
		}

		return remainingEnergy;
	}

	public static int[] rationPower(int energy, int startX, int startY, int bannedX, int bannedY,
			World world, int[] array, LongMap traversed) {
		int[] energyEach = array;
		int connectionsFound = 0;

		// find connections by setting points that correspond with offsets to 0
		for (int i = 0; i < OFFSETS.length; i++) {
			if (world.getBlock(startX + OFFSETS[i].getX(), startY + OFFSETS[i].getY()) instanceof IPowerHandler
					|| world.getBlock(startX + OFFSETS[i].getX(), startY + OFFSETS[i].getY()) instanceof BlockPowerHandler) {
				if (startX + OFFSETS[i].getX() == bannedX && startY + OFFSETS[i].getY() == bannedY) continue;
				if(traversed.get(Utils.packLong(startX + OFFSETS[i].getX(), startY + OFFSETS[i].getY())) != null){
					continue;
				}
				
				traversed.put(Utils.packLong(startX + OFFSETS[i].getX(), startY + OFFSETS[i].getY()), true);
				connectionsFound++;
				energyEach[i] = 0;
			}
		}

		if(connectionsFound <= 0) return energyEach;
		
		boolean first = true;
		// ration energy to each connection
		for (int i = 0; i < OFFSETS.length; i++) {
			if (energyEach[i] > -1) {
				Block current = world.getBlock(startX + OFFSETS[i].getX(),
						startY + OFFSETS[i].getY());
				IPowerHandler connector = null;
				if (current instanceof BlockPowerHandler) {
					connector = ((BlockPowerHandler) current).getPowerHandler(world, startX
							+ OFFSETS[i].getX(), startY + OFFSETS[i].getY());
				} else if (current instanceof IPowerHandler) {
					connector = (IPowerHandler) current;
				}
				
				energyEach[i] = Math.min(connector.getMaxReceive(), energy / connectionsFound);
				if(first){
					first = false;
					energyEach[i] += energy % connectionsFound;
				}
			}
		}

		return energyEach;
	}

	public static void neg1Array(int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = -1;
		}
	}

}
