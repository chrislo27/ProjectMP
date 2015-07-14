package projectmp.common.item;

import projectmp.client.WorldRenderer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * This item can break blocks by holding down the use key. 
 * 
 *
 */
public class ItemMineable extends Item{

	public ItemMineable(String unlocalizedname) {
		super(unlocalizedname);
	}
	
	private Color outline = new Color(0f, 1f, 1f, 1f);
	
	@Override
	public void onRenderCursorBlock(WorldRenderer renderer, ItemStack stack, int x, int y){
		ShaderProgram shader = renderer.main.maskNoiseShader;
		
		renderer.batch.setShader(shader);
		
		shader.setUniformf("speed", 0f);
		shader.setUniformf("time", renderer.main.totalSeconds);
		shader.setUniformf("outlinecolor", outline);
		Utils.setupMaskingNoiseShader(shader, (MathHelper.getNumberFromTime(4)));
		
		super.onRenderCursorBlock(renderer, stack, x, y);
		
		renderer.batch.setShader(null);
	}

}
