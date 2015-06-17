package projectmp.common.gui;

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
		register("test", new TestGui());
	}
	
	public void register(String name, GuiHandler gui){
		map.put(name, gui);
	}
	
	public GuiHandler get(String name){
		return map.get(name);
	}

}
