package projectmp.common.world.background;

import projectmp.common.world.Time.TimeOfDay;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;


public class Background {

	private World world;
	
	private float fullnessOfBg = 1f;
	
	/**
	 * used to generate the fade into effect
	 */
	private TimeOfDay currentKnownTOD = TimeOfDay.DAYTIME;
	/**
	 * the time of day BEFORE currentKnownTOD
	 */
	private TimeOfDay lastTOD = TimeOfDay.DAYTIME;
	
	public Background(World w){
		this.world = w;
	}
	
	public void render(){
		if(world.worldTime.getCurrentTimeOfDay() != currentKnownTOD){
			currentKnownTOD = world.worldTime.getCurrentTimeOfDay();
			lastTOD = getTimeOfDayBefore();
			fullnessOfBg = 0;
		}
		
		if(fullnessOfBg < 1){
			fullnessOfBg += Gdx.graphics.getDeltaTime() * 2;
			
			if(fullnessOfBg > 1) fullnessOfBg = 1;
		}
		
		// render the time of day that's BEFORE the current one
		lastTOD.renderBackground(world.batch, world);
		// then render the current one on top with an alpha (for the transition)
		world.batch.setColor(1, 1, 1, fullnessOfBg);
		world.worldTime.getCurrentTimeOfDay().renderBackground(world.batch, world);
		world.batch.setColor(1, 1, 1, 1);
	}
	
	private TimeOfDay getTimeOfDayBefore(){
		TimeOfDay[] values = TimeOfDay.values();
		
		int index = 0;
		for(int i = 0; i < values.length; i++){
			if(world.worldTime.getCurrentTimeOfDay() == values[i]){
				index = i - 1;
				if(index < 0) index = values.length - 1;
			}
		}
		
		return values[index];
	}
	
}
