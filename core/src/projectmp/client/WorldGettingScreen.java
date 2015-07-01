package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.Translator;

import com.badlogic.gdx.math.MathUtils;

public class WorldGettingScreen extends MessageScreen {

	public WorldGettingScreen(Main m) {
		super(m);
	}

	public float percentReceived = 0f;
	public float percentEach = 0;
	public boolean isSingleplayer = false;

	public void addPercent() {
		percentReceived = MathUtils.clamp(percentReceived + percentEach, 0f, 1f);
	}

	@Override
	public String getRenderMessage() {
		return getMessage() + String.format("%.1f", percentReceived * 100f) + "%";
	}

	@Override
	public void show() {
		setMessage(Translator.instance().getMsg("menu.msg.gettingworld." + (isSingleplayer ? "singleplayer" : "online")));
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

}
