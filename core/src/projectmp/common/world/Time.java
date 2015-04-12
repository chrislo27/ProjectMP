package projectmp.common.world;

import com.badlogic.gdx.graphics.Color;

import projectmp.common.Main;

public class Time {

	public int days = 0;
	public int totalTicks = 0;
	public int currentDayTicks = 0;

	public final int ticksPerDay = Math.round(Main.TICKS * 30);
	public final float daytimePercentage = 0.5f;
	public final float eveningPercentage = 0.1f;
	public final float nighttimePercentage = 0.4f;

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
	}

	public TimeOfDay getCurrentTimeOfDay() {
		if (currentDayTicks <= (daytimePercentage * ticksPerDay)) {
			return TimeOfDay.DAYTIME;
		} else if (currentDayTicks <= (daytimePercentage * ticksPerDay)
				+ (eveningPercentage * ticksPerDay)) {
			return TimeOfDay.EVENING;
		} else {
			return TimeOfDay.NIGHTTIME;
		}
	}

	public static enum TimeOfDay {
		DAYTIME((byte) 127, Color.rgb888(0, 0, 0)), EVENING((byte) 64, Color.rgb888(1, 174 / 255f, 0)), NIGHTTIME(
				(byte) 16, Color.rgb888(0, 0, 0));

		public byte lightLevel = 127;
		public int color = Color.rgb888(0, 0, 0);

		private TimeOfDay(byte light, int color) {
			lightLevel = light;
			this.color = color;
		}
	}

}
