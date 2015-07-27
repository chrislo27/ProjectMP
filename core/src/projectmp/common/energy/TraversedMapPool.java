package projectmp.common.energy;

import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;


public final class TraversedMapPool {

	private TraversedMapPool(){}
	
	private static final Pool<LongMap> pool = Pools.get(LongMap.class, 32);
	
	public static LongMap get(){
		LongMap m = pool.obtain();
		m.clear();
		return m;
	}
	
	public static void free(LongMap m){
		if(m == null) return;
		
		m.clear();
		pool.free(m);
	}

}
