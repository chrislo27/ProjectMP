package projectmp.common.world;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.util.AssetMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Time {

	public static final float daytimePercentage = 0.5f;
	public static final float eveningPercentage = 0.1f;
	public static final float nighttimePercentage = 0.4f;
	
	public int days = 0;
	public int totalTicks = 0;
	public int currentDayTicks = 0;

	public final int ticksPerDay = Math.round(Main.TICKS * 30);

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

}
