package projectmp.common.world;

import com.badlogic.gdx.graphics.Color;

import projectmp.common.Main;

public class Time {

	public int days = 0;
	public int totalTicks = 0;
	public int currentDayTicks = 0;

	public final int ticksPerDay = Math.round(Main.TICKS * 30);
	public static final float daytimePercentage = 0.5f;
	public static final float eveningPercentage = 0.1f;
	public static final float nighttimePercentage = 0.4f;

	public Time() {

	}

	public void tickUpdate() {
		totalTicks++;
		currentDayTicks++;

		while (currentDayTicks > ticksPerDay) {
			currentDayTicks -= ticksPerDay;
			days++;
		}
	}

	public void setTotalTime(int t) {
		totalTicks = t;
		currentDayTicks = totalTicks % ticksPerDay;
		days = totalTicks / ticksPerDay;
	}

	public TimeOfDay getCurrentTimeOfDay() {
		return TimeOfDay.getTimeOfDayFromPercentage(currentDayTicks / (ticksPerDay * 1f));
	}

	public static enum TimeOfDay {
		DAYTIME((byte) 127, Color.rgb888(0, 0, 0), Time.daytimePercentage), EVENING((byte) 64,
				Color.rgb888(1, 174 / 255f, 0), Time.eveningPercentage), NIGHTTIME((byte) 16, Color
				.rgb888(0, 0, 0), Time.nighttimePercentage);

		public byte lightLevel = 127;
		public int color = Color.rgb888(0, 0, 0);
		public float percentage = 0f;

		private TimeOfDay(byte light, int color, float percent) {
			lightLevel = light;
			this.color = color;
			percentage = percent;
		}
		
		public static float getTotalPercentage(){
			float f = 0;
			
			for(TimeOfDay t : TimeOfDay.values()){
				f += t.percentage;
			}
			
			return f;
		}
		
		public static TimeOfDay getTimeOfDayFromPercentage(float percentage){
			float totalPercentage = getTotalPercentage();
			
			float curper = 0;
			TimeOfDay[] values = TimeOfDay.values();
			for(int i = 0; i < values.length; i++){
				TimeOfDay tod = values[i];
				
				if(percentage >= curper && percentage <= curper + tod.percentage){
					return tod;
				}
				
				curper += tod.percentage;
			}
			
			return null;
		}
		
	}

}
