package stray.blocks;

import stray.world.World;


public interface AffectsColour {

	public boolean colourOn(World world, int x, int y);
	
	public String getColour(World world, int x, int y);
	
}
