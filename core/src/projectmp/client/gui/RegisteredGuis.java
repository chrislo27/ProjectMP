package projectmp.client.gui;

import java.util.HashMap;

import projectmp.common.util.sidedictation.Side;
import projectmp.common.util.sidedictation.SideOnly;

@SideOnly(Side.CLIENT)
public class RegisteredGuis {

	private static RegisteredGuis instance;

	private RegisteredGuis() {
	}

	public static RegisteredGuis instance() {
		if (instance == null) {
			instance = new RegisteredGuis();
			instance.loadResources();
		}
		return instance;
	}

	private HashMap<String, GuiHandler> map = new HashMap<String, GuiHandler>();

	private void loadResources() {

	}

}
