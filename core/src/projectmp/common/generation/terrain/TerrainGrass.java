package projectmp.common.generation.terrain;

import projectmp.common.block.Blocks;
import projectmp.common.util.SimplexNoise;
import projectmp.common.world.World;


public class TerrainGrass extends TerrainGenerationGroup{

	@Override
	public void generate(World world, SimplexNoise noiseGen) {
		for(int x = 0; x < world.sizex; x++){
			int y = 0;
			while(y < world.sizey){
				if(world.getBlock(x, y) == Blocks.instance().getBlock("dirt")){
					world.setBlock("grass", x, y);
					break;
				}
				y++;
			}
		}
	}

}
