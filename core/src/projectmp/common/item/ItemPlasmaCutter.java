package projectmp.common.item;

import projectmp.client.WorldRenderer;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.util.render.ElectricityFX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class ItemPlasmaCutter extends ItemMineable {

	public ItemPlasmaCutter(String unlocalizedname) {
		super(unlocalizedname);
	}

	@Override
	public void onUsingRender(WorldRenderer renderer, EntityPlayer player, int slot, int x,
			int y) {
		super.onUsingRender(renderer, player, slot, x, y);

		renderer.startBypassing();
		
		for(int i = 0; i < Math.max(Gdx.graphics.getFramesPerSecond() / 30, 1); i++){
			ElectricityFX.drawElectricity(renderer.batch,
					renderer.convertWorldX(player.visualX + (player.sizex / 2f)),
					renderer.convertWorldY(player.visualY + (player.sizey / 2f), 0),
					renderer.convertWorldX(x + 0.5f), renderer.convertWorldY(y + 0.5f, 0),
					ElectricityFX.DEFAULT_OFFSET_OF_POINTS * 0.75f, ElectricityFX.DEFAULT_THICKNESS * 0.75f,
					ElectricityFX.DEFAULT_DISTANCE_BETWEEN_POINTS, ElectricityFX.getDefaultColor(1f));
		}
		
		renderer.stopBypassing();
	}
	
	@Override
	public void addDescription(Array<String> array, ItemStack stack){
		array.add("[RED]markup test[]");
		array.add("This item mines blocks");
	}
}
