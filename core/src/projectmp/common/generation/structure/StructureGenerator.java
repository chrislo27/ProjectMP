package projectmp.common.generation.structure;

import projectmp.common.generation.GenerationGroup;


public abstract class StructureGenerator extends GenerationGroup{

	public StructureGenerator() {
		super(GenerationGroup.PRIORITY_STRUCTURE);
	}

}
