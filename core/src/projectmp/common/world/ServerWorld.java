package projectmp.common.world;

import projectmp.common.Main;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.entity.EntityLiving;
import projectmp.common.packet.PacketBlockUpdate;
import projectmp.common.packet.PacketSendTileEntity;
import projectmp.common.packet.PacketTimeUpdate;
import projectmp.common.packet.PacketUpdateHealth;
import projectmp.common.packet.PacketWeather;
import projectmp.common.packet.repository.PacketRepository;
import projectmp.common.registry.WeatherRegistry;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.weather.Weather;
import projectmp.server.ServerLogic;

public class ServerWorld extends World {

	ServerLogic logic;
	boolean shouldSendUpdates = true;

	public ServerWorld(Main main, int x, int y, long seed, ServerLogic l) {
		super(main, x, y, true, seed);
		logic = l;
	}

	public void setSendingUpdates(boolean b) {
		shouldSendUpdates = b;
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();
		if (time.totalTicks % (Main.TICKS * 5) == 0 && logic.server.getConnections().length > 0) {
			sendTimeUpdate();
		}
	}

	public void sendTimeUpdate() {
		PacketTimeUpdate timepacket = PacketRepository.instance().timeUpdate;
		
		timepacket.totalTicks = time.totalTicks;
		logic.server.sendToAllTCP(timepacket);
	}

	public void sendHealthUpdate(EntityLiving e) {
		PacketUpdateHealth healthpacket = PacketRepository.instance().updateHealth;
		
		healthpacket.uuid = e.uuid;
		healthpacket.newhealth = e.health;
		logic.server.sendToAllTCP(healthpacket);
	}

	public void sendWeatherPacket() {
		PacketWeather weatherpacket = PacketRepository.instance().weatherUpdate;
		
		weatherpacket.weatherDuration = getWeather().getTotalDuration();
		if (getWeather() == null) {
			weatherpacket.weatherType = null;
		} else {
			weatherpacket.weatherType = WeatherRegistry.instance().getWeatherKey(
					getWeather().getClass());
		}

		logic.server.sendToAllTCP(weatherpacket);
	}

	@Override
	public void setWeather(Weather w) {
		super.setWeather(w);

		sendWeatherPacket();
	}

	@Override
	public void setBlock(Block b, int x, int y) {
		Block old = getBlock(x, y);
		super.setBlock(b, x, y);
		if (getBlock(x, y) != old) sendBlockUpdatePacket(x, y);
	}

	@Override
	public void setMeta(int m, int x, int y) {
		int old = getMeta(x, y);
		super.setMeta(m, x, y);
		if (getMeta(x, y) != old) sendBlockUpdatePacket(x, y);
	}

	@Override
	public void setTileEntity(TileEntity te, int x, int y) {
		super.setTileEntity(te, x, y);
		sendTileEntityUpdate(x, y);
	}

	private void sendBlockUpdatePacket(int x, int y) {
		if (!shouldSendUpdates) return;
		PacketBlockUpdate bupacket = PacketRepository.instance().blockUpdate;
		
		bupacket.block = Blocks.instance().getKey(getBlock(x, y));
		bupacket.meta = getMeta(x, y);
		bupacket.x = x;
		bupacket.y = y;

		logic.server.sendToAllTCP(bupacket);
	}

}
