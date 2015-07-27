package projectmp.common.generation.terrain;

import projectmp.common.generation.GenerationGroup;


public abstract class TerrainGenerationGroup extends GenerationGroup{

	public TerrainGenerationGroup() {
		super(GenerationGroup.PRIORITY_TERRAIN);
	}

}
