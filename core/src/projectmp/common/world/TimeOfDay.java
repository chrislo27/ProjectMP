package projectmp.common.world;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.util.AssetMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public enum TimeOfDay {

	DAYTIME((byte) 127, Color.rgb888(0, 0, 0), Time.daytimePercentage) {

		@Override
		public void renderBackground(Batch batch, World world) {
			batch.setColor(0.4f, 0.4f, 0.6f, batch.getColor().a);
			Main.fillRect(batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
			batch.setColor(1, 1, 1, 1);
		}
	},

	EVENING((byte) 64, Color.rgb888(1, 174 / 255f, 0), Time.eveningPercentage) {

		@Override
		public void renderBackground(Batch batch, World world) {
			batch.setColor(1, 137f / 255f, 41f / 255f, batch.getColor().a);
			Main.fillRect(batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
			batch.setColor(1, 1, 1, 1);
		}
	},

	NIGHTTIME((byte) 16, Color.rgb888(0, 0, 0), Time.nighttimePercentage) {

		@Override
		public void renderBackground(Batch batch, World world) {
			batch.draw(AssetRegistry.getTexture("starrysky"), 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		}
	};

	public byte lightLevel = 127;
	public int color = Color.rgb888(0, 0, 0);
	public float percentage = 0f;

	private TimeOfDay(byte light, int color, float percent) {
		lightLevel = light;
		this.color = color;
		percentage = percent;
	}

	public void renderBackground(Batch batch, World world) {

	}

	public static float getTotalPercentage() {
		float f = 0;

		for (TimeOfDay t : TimeOfDay.values()) {
			f += t.percentage;
		}

		return f;
	}

	public static TimeOfDay getTimeOfDayFromPercentage(float percentage) {
		float totalPercentage = getTotalPercentage();

		float curper = 0;
		TimeOfDay[] values = TimeOfDay.values();
		for (int i = 0; i < values.length; i++) {
			TimeOfDay tod = values[i];

			if (percentage >= curper && percentage <= curper + tod.percentage) {
				return tod;
			}

			curper += tod.percentage;
		}

		return null;
	}

}