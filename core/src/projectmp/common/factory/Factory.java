package projectmp.common.factory;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;

/**
 * Factories are singleton objects that use Blueprints to create objects out of String keys.
 * <br>
 * They are designed in such a way that they can have external (modded) blueprints added to them.
 * 
 *
 */
public abstract class Factory<T> {

	private Array<Blueprint> blueprints = new Array<>();
	
	public void addBlueprint(Blueprint bp){
		blueprints.add(bp);
	}
	
	public Array<Blueprint> getBlueprints(){
		return blueprints;
	}
	
	public T manufacture(String key){
		
		for(int i = 0; i < blueprints.size; i++){
			Blueprint<T> bp = blueprints.get(i);
			T returnItem = null;
			
			returnItem = bp.manufacture(key);
			
			if(returnItem != null){
				return returnItem;
			}
		}
		
		return null;
	}
	
}
