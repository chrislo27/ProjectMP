package projectmp.common.generation.terrain;

import projectmp.common.block.Blocks;
import projectmp.common.util.SimplexNoise;
import projectmp.common.world.World;


public class TerrainLandscape extends TerrainGenerationGroup{

	@Override
	public void generate(World world, SimplexNoise noiseGen) {
		float[] elevation = new float[world.sizex];
		
		float hillCenterPoint = world.sizey / 4; // the "0" point of a hill, noise is -1 to 1
		float hillHeightCoefficient = 16; // the maximum height of a hill from the "0" point
		
		for(int i = 0; i < elevation.length; i++){
			elevation[i] = (float) noiseGen.eval(i * 0.05, -1);
			
			int start = (int) (hillCenterPoint + elevation[i] * hillHeightCoefficient);
			for(int y = start; y < world.sizey; y++){
				world.setBlock("stone", i, y);
				
				if(y >= start && y <= start + (hillHeightCoefficient / 2)){
					world.setBlock("dirt", i, y);
				}
			}
		}
		
	}

}
