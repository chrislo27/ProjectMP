package projectmp.client.gui;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.inventory.Slot;
import projectmp.common.util.sidedictation.Side;
import projectmp.common.util.sidedictation.SideOnly;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

@SideOnly(Side.CLIENT)
public class GuiHandler {

	Array<Slot> slots = new Array<>();
	
	public GuiHandler() {

	}
	
	public void render(WorldRenderer renderer){
		
	}
	
	public void renderDarkOverlay(SpriteBatch batch){
		float oldColor = batch.getColor().toFloatBits();
		
		batch.setColor(0, 0, 0, 0f);
		Main.fillRect(batch, 0, 0, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch.setColor(oldColor);
	}

}
