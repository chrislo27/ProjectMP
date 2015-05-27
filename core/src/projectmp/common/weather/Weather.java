package projectmp.common.weather;

import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.Batch;


public abstract class Weather {

	/**
	 * the time remaining in ticks. updated by world in its own tickupdate method
	 */
	protected int ticksRemaining;
	
	/**
	 * the length of the weather
	 */
	protected int originalDuration;
	
	protected World world;
	
	public Weather(int duration, World world){
		originalDuration = duration;
		ticksRemaining = duration;
		
		this.world = world;
	}
	
	public abstract void renderOverBackground(Batch batch);
	
	public abstract void tickUpdate();
	
	public abstract void renderOnWorld(Batch batch);
	
	public abstract void renderHUD(Batch batch);
	
	public void onStart(){
		
	}
	
	public void onFinish(){
		
	}
	
	public void setTimeRemaining(int timeRemaining){
		if(timeRemaining > originalDuration){
			originalDuration = timeRemaining;
		}
		ticksRemaining = timeRemaining;
	}
	
	public int getTimeRemaining(){
		return ticksRemaining;
	}
	
	public int getTotalDuration(){
		return originalDuration;
	}
	
	public void tickDownTimeRemaining(){
		if(ticksRemaining == originalDuration){
			onStart();
		}
		
		ticksRemaining--;
		
		if(ticksRemaining == 0){
			onFinish();
		}
	}
	
}
