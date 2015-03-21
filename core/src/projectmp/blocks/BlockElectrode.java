package projectmp.blocks;

import projectmp.Main;
import projectmp.entity.Entity;
import projectmp.entity.EntityLiving;
import projectmp.util.DamageSource;
import projectmp.util.MathHelper;
import projectmp.util.render.ElectricityRenderer;
import projectmp.world.World;

import com.badlogic.gdx.math.MathUtils;

public class BlockElectrode extends Block {

	public BlockElectrode(String path) {
		super(path);
		this.useConTextures();
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		for (Entity e : world.entities) {
			if (e instanceof EntityLiving) {
				if (Block.entityIntersects(world, x, y, e)) ((EntityLiving) e).damage(1, DamageSource.electric);
			}
		}
	}

	@Override
	public void render(World world, int x, int y) {
		super.render(world, x, y);
		
		for(int searchx = x; searchx > x - 8; searchx--){
			for(int searchy = y + 9; searchy > y - 8; searchy--){
				if(searchx == x && searchy == y) continue;
				if(world.getBlock(searchx, searchy) instanceof BlockElectrode){
					ElectricityRenderer.drawP2PLightning(world.batch, (x + 0.5f) * World.tilesizex
							- world.camera.camerax, Main.convertY((y + 0.5f) * World.tilesizey - world.camera.cameray),
							(searchx + 0.5f) * World.tilesizex - world.camera.camerax, Main.convertY((searchy + 0.5f)
									* World.tilesizey - world.camera.cameray), World.tilesizex / 2f, 5, 3,
							2, ElectricityRenderer.getDefaultColor(1));
				}
			}
		}
		
	}
	
}
