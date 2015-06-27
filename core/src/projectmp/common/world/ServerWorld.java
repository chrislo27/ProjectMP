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
import projectmp.common.registry.WeatherRegistry;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.weather.Weather;
import projectmp.server.ServerLogic;

public class ServerWorld extends World {

	ServerLogic logic;
	private PacketBlockUpdate bupacket = new PacketBlockUpdate();
	private PacketTimeUpdate timepacket = new PacketTimeUpdate();
	private PacketUpdateHealth healthpacket = new PacketUpdateHealth();
	private PacketWeather weatherpacket = new PacketWeather();
	private PacketSendTileEntity tepacket = new PacketSendTileEntity();
	boolean shouldSendUpdates = true;

	public ServerWorld(Main main, int x, int y, boolean server, long seed, ServerLogic l) {
		super(main, x, y, server, seed);
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
		
		for(int x = 0; x < sizex; x++){
			for(int y = sizey - 1; y > 0; y--){
				if(getTileEntity(x, y) != null){
					if(getTileEntity(x, y).isDirty()){
						sendTileEntityUpdate(x, y);
						getTileEntity(x, y).setDirty(false);
					}
				}
			}
		}
	}

	public void sendTimeUpdate() {
		timepacket.totalTicks = time.totalTicks;
		logic.server.sendToAllTCP(timepacket);
	}

	public void sendHealthUpdate(EntityLiving e) {
		healthpacket.uuid = e.uuid;
		healthpacket.newhealth = e.health;
		logic.server.sendToAllTCP(healthpacket);
	}

	public void sendWeatherPacket() {
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

	private void sendTileEntityUpdate(int x, int y) {
		tepacket.te = getTileEntity(x, y);
		tepacket.x = x;
		tepacket.y = y;

		logic.server.sendToAllTCP(tepacket);
	}

	private void sendBlockUpdatePacket(int x, int y) {
		if (!shouldSendUpdates) return;
		bupacket.block = Blocks.instance().getKey(getBlock(x, y));
		bupacket.meta = getMeta(x, y);
		bupacket.x = x;
		bupacket.y = y;

		logic.server.sendToAllTCP(bupacket);
	}

}
