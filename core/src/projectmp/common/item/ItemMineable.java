package projectmp.common.item;

import projectmp.common.Main;
import projectmp.common.block.Blocks;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.packet.PacketBlockUpdate;
import projectmp.common.packet.PacketBreakingProgress;
import projectmp.common.packet.repository.PacketRepository;
import projectmp.common.util.GameException;
import projectmp.common.world.World;


/**
 * This item can break blocks by holding down the use key. 
 * 
 *
 */
public class ItemMineable extends Item{

	public ItemMineable(String unlocalizedname) {
		super(unlocalizedname);
		
		this.setMaxStack(1);
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
	public void onUsing(World world, EntityPlayer player, int slot, int x, int y){
		if(world.isServer == false){
			PacketBreakingProgress packet = PacketRepository.instance().breakingProgress;
			packet.x = x;
			packet.y = y;
			float progress = world.getBreakingProgress(x, y);
			
			if(effectiveness <= 0){
				throw new GameException("Item mining effectiveness cannot be <= 0!");
			}
			if(world.getBlock(x, y).getHardness() < 0){
				world.setBreakingProgress(x, y, 0);
				
				return;
			}
			
			world.setBreakingProgress(x, y, progress + ((world.getBlock(x, y).getHardness() / effectiveness) / Main.TICKS) + (World.BLOCK_RECEDE / Main.TICKS));
			
			float newProgress = world.getBreakingProgress(x, y);
			
			if(newProgress >= 1f){
				packet.progress = 0;
				
				world.setBreakingProgress(x, y, 0);
				
				PacketBlockUpdate bu = PacketRepository.instance().blockUpdate;
				bu.block = "empty";
				bu.meta = 0;
				bu.x = x;
				bu.y = y;
				
				world.main.client.sendTCP(bu);
				world.main.client.sendTCP(packet);
			}else{
				packet.progress = newProgress;
				world.main.client.sendUDP(packet);
			}
		}
	}

}
