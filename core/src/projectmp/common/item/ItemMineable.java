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
	
	/**
	 * A tool with 1.0 effectiveness can mine a block with 1.0 hardness in 1.0 seconds.
	 * <br>
	 * A tool with 2.0 effectiveness can mine a block with 1.0 hardness in 0.5 seconds.
	 * <br>
	 * That means the time it takes to mine a block is hardness / effectiveness (plus compensation for block recede)
	 */
	public float effectiveness = 1f;
	
	@Override
	public void onUsing(World world, EntityPlayer player, ItemStack stack){
		if(world.isServer == false){
			int x = world.main.clientLogic.getCursorBlockX();
			int y = world.main.clientLogic.getCursorBlockY();
			
			float progress = world.getBreakingProgress(x, y);
			
			world.setBreakingProgress(x, y, progress + (0.1f) + (World.BLOCK_RECEDE / Main.TICKS));
		}
	}

}
