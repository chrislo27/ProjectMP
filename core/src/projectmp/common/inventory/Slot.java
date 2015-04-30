package projectmp.common.inventory;

import projectmp.common.world.World;


public class Slot {

	ItemStack item = new ItemStack(null, 0);
	
	int posx = 0;
	int posy = 0;
	int width = World.tilesizex;
	int height = World.tilesizey;
	
}
