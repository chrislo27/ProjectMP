package projectmp.client;

import projectmp.common.Main;

import com.badlogic.gdx.math.MathUtils;


public class WorldGettingScreen extends MessageScreen{

	public WorldGettingScreen(Main m) {
		super(m);
		setMessage("Receiving world data: ");
	}
	
	public float percentReceived = 0f;
	public float percentEach = 0;
	
	public void addPercent(){
		percentReceived = MathUtils.clamp(percentReceived + percentEach, 0f, 1f);
	}
	
	@Override
	public String getRenderMessage(){
		return getMessage() + String.format("%.1f", percentReceived) + "%";
	}

}
