package projectmp.common.item;

import projectmp.client.WorldRenderer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.util.MathHelper;

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
	
	@Override
	public void onRenderCursorBlock(WorldRenderer renderer, ItemStack stack, int x, int y){
		ShaderProgram shader = renderer.main.maskNoiseShader;
		
		renderer.batch.setShader(shader);
		
		shader.setUniformf("zoom", 2f);
		shader.setUniformf("intensity", 2.5f - (MathHelper.getNumberFromTime(4) * 2.5f));
		shader.setUniformf("speed", 0f);
		shader.setUniformf("time", renderer.main.totalSeconds);
		
		super.onRenderCursorBlock(renderer, stack, x, y);
		
		renderer.batch.setShader(null);
	}

}
