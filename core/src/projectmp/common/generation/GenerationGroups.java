package projectmp.common.generation;

import projectmp.common.generation.terrain.TerrainLandscape;
import projectmp.common.world.World;

import com.badlogic.gdx.utils.Array;

/**
 * Singleton of all the registered generation groups.
 * 
 *
 */
public class GenerationGroups {

	private static GenerationGroups instance;

	private GenerationGroups() {
	}

	public static GenerationGroups instance() {
		if (instance == null) {
			instance = new GenerationGroups();
			instance.loadResources();
		}
		return instance;
	}

	private Array<GenerationGroup> groups = new Array<>();
	
	private void loadResources() {
		// add stock
		addGroup(new TerrainLandscape());
	}
	
	/**
	 * Adds and sorts a group
	 * @param g
	 */
	public synchronized void addGroup(GenerationGroup g){
		groups.add(g);
		groups.sort();
	}
	
	public synchronized Array<GenerationGroup> getAllGroups(){
		return groups;
	}
	
	public static void generateWorld(World world){
		for(int i = 0; i < instance().groups.size; i++){
			instance().groups.get(i).generate(world, world.noiseGen);
		}
	}
	
}
