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

}
