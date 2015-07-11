package projectmp.common.generation.feature;

import projectmp.common.generation.GenerationGroup;


public abstract class FeatureGenerator extends GenerationGroup{

	public FeatureGenerator() {
		super(GenerationGroup.PRIORITY_FEATURE);
	}

}
