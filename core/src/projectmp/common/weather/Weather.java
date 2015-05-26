package projectmp.common.weather;

import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.Batch;


public abstract class Weather {

	/**
	 * the time remaining in ticks. updated by world in its own tickupdate method
	 */
	protected int ticksRemaining;
	
	public abstract void renderAboveBackground(Batch batch, World world);
	
	public abstract void tickUpdate(World world);
	
	public int getTimeRemaining(){
		return ticksRemaining;
	}
	
}
