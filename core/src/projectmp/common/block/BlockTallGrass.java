package projectmp.common.block;

import java.util.ArrayList;

import projectmp.common.Main;
import projectmp.common.entity.Entity;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Sizeable;
import projectmp.common.world.World;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class BlockTallGrass extends BlockFoliage {

	public BlockTallGrass(String identifier) {
		super(identifier);
	}

	@Override
	public int getRenderingLayer(World world, int x, int y){
		return 1;
	}
	
}
