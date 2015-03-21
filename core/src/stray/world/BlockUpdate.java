package stray.world;

import com.badlogic.gdx.utils.Pool.Poolable;

import stray.blocks.Block;
import stray.blocks.Blocks;


public class BlockUpdate implements Poolable{
	
	protected int x;
	protected int y = 0;
	
	protected Block block = Blocks.defaultBlock();
	protected int meta = 0;
	
	public BlockUpdate(){
		
	}
	
	public BlockUpdate init(int x, int y, Block b, int m){
		this.x = x;
		this.y = y;
		block = b;
		meta = m;
		return this;
	}
	

	@Override
	public void reset() {
		meta = 0;
		block = Blocks.defaultBlock();
		x = y = 0;
	}
}
