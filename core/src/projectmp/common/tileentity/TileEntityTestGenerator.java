package projectmp.common.tileentity;

import projectmp.common.Main;
import projectmp.common.energy.EnergyChain;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;


public class TileEntityTestGenerator extends TileEntityPowerHandler{

	public TileEntityTestGenerator(){
		super();
	}
	
	public TileEntityTestGenerator(int x, int y){
		super(x, y);
		this.setMaxCapacity(0);
	}
	
	@Override
	public void tickUpdate(World world){
		if(world.time.totalTicks % Main.TICKS != 0) return;
		if(!Gdx.input.isKeyPressed(Keys.ENTER)) return;
		
		Main.logger.debug("leftover: " + EnergyChain.doSpreadPower(16, x, y, world));
	}
	
}
