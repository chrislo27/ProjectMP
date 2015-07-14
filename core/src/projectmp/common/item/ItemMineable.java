package projectmp.common.item;

import projectmp.common.Main;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;


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
	public void onUsing(World world, EntityPlayer player, ItemStack stack){
		if(world.isServer == false){
			int x = world.main.clientLogic.getCursorBlockX();
			int y = world.main.clientLogic.getCursorBlockY();
			
			float progress = world.getBreakingProgress(x, y);
			
			world.setBreakingProgress(x, y, progress + 0.5f * Gdx.graphics.getDeltaTime());
		}
	}

}
