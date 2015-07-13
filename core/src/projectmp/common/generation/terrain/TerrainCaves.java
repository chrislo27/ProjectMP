package projectmp.common.generation.terrain;

import projectmp.common.util.SimplexNoise;
import projectmp.common.world.World;


public class TerrainCaves extends TerrainGenerator{

	@Override
	public void generate(World world, SimplexNoise noiseGen) {
		double amplitude = 0.05;
		double greaterThan = 0.45;
		double lessThan = 0.75;
		
		for(int x = 0; x < world.sizex; x++){
			for(int y = 0; y < world.sizey; y++){
				float check1 = (float) noiseGen.eval(x * amplitude, y * amplitude * 1.5);
				float check2 = (float) noiseGen.eval(-x * amplitude, -y * amplitude * 1.5);
				
				check1 = (check1 + 1) / 2f;
				check2 = (check2 + 1) / 2f;
				
				if((check1 > greaterThan && check1 < lessThan) && (check2 > greaterThan && check2 < lessThan)){
					world.setBlock(null, x, y);
				}
			}
		}
	}

}
