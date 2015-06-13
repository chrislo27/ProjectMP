package projectmp.common.inventory;

import projectmp.common.world.World;

public class Slot {

	int posx = 0;
	int posy = 0;
	int width = World.tilesizex;
	int height = World.tilesizey;
	int slotNum = -1;
	
	public Slot(int slotNumber, int x, int y){
		slotNum = slotNumber;
		posx = x;
		posy = y;
	}

}
