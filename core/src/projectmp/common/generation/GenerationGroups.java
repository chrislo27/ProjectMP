package projectmp.common.generation;

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
		
	}
	
	/**
	 * Adds and sorts a group
	 * @param g
	 */
	public synchronized void addGroup(GenerationGroup g){
		addGroup(g);
		groups.sort();
	}
	
	public synchronized Array<GenerationGroup> getAllGroups(){
		return groups;
	}
	
}
