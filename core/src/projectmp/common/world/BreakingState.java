package projectmp.common.world;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;


public class BreakingState implements Poolable {

	private static final Pool<BreakingState> pool = Pools.get(BreakingState.class);

	public int x;
	public int y;
	public float progress;
	
	public BreakingState set(int x, int y, float progress){
		this.x = x;
		this.y = y;
		this.progress = progress;
		
		return this;
	}
	
	@Override
	public void reset() {
		progress = 0f;
	}
	
	public static BreakingState obtain(int x, int y, float progress){
		return pool.obtain().set(x, y, progress);
	}
	
}
