package projectmp.common.generation;

import projectmp.common.util.SimplexNoise;
import projectmp.common.world.World;

/**
 * World generators implement this. It has a compareTo method (from Comparable) which compares priority for the order in which
 * the generators to executed in. Priority cannot be less than 0.
 * 
 *
 */
public abstract class GenerationGroup implements Comparable<GenerationGroup>{

	public static final int PRIORITY_TERRAIN = 0;
	public static final int PRIORITY_FEATURE = 1;
	public static final int PRIORITY_STRUCTURE = 2;
	
	/**
	 * Cannot be less than 0, the setter will clamp to 0 AND the compareTo method will treat 0 and below equally.
	 */
	private int priority = 0;
	
	public GenerationGroup(int priority){
		setPriority(priority);
	}
	
	public abstract void generate(World world, SimplexNoise noiseGen);
	
	/**
	 * Descending order, so lower priorities are first.
	 */
	@Override
	public int compareTo(GenerationGroup other) {
		// 0 and below compared with 0 and below are equal
		if(other.getPriority() <= 0 && this.getPriority() <= 0) return 0;
		
		return other.getPriority() - this.getPriority();
	}
	
	public int getPriority(){
		return priority;
	}
	
	public void setPriority(int p){
		priority = Math.max(p, 0);
	}

}
