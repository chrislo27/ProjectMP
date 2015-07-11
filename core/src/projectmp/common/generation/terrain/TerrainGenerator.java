package projectmp.common.generation.terrain;

import projectmp.common.generation.GenerationGroup;


public abstract class TerrainGenerator extends GenerationGroup{

	public TerrainGenerator() {
		super(GenerationGroup.PRIORITY_TERRAIN);
	}

}
