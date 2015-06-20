package projectmp.common.packet;

import java.lang.reflect.InvocationTargetException;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.registry.WeatherRegistry;
import projectmp.common.world.World;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketWeather implements Packet {

	public String weatherType = null;
	public int weatherDuration;
	public int weatherTimeRemaining;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		if (weatherType == null) {
			logic.world.setWeather(null);
		} else {
			try {
				logic.world.setWeather(WeatherRegistry.instance().getRegistry().getValue(weatherType)
						.getConstructor(int.class, World.class)
						.newInstance(weatherDuration, logic.world));
				logic.world.getWeather().setTimeRemaining(weatherTimeRemaining);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				Main.logger.error("Failed to re-create weather instance client-side", e);

				Main.ERRORMSG.setMessage("Failed to re-create weather instance client-side\n"
						+ e.getMessage());
				logic.main.setScreen(Main.ERRORMSG);
			}
		}
	}
}
