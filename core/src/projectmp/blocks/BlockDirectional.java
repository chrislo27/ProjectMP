package projectmp.blocks;

import projectmp.Main;
import projectmp.blocks.Block.BlockFaces;
import projectmp.util.MathHelper;
import projectmp.util.Utils;
import projectmp.world.World;

import com.badlogic.gdx.graphics.Texture;


public class BlockDirectional extends Block{

	public BlockDirectional(String path, boolean first) {
		super(path);
		findFirstMatch = first;
	}
	
	boolean findFirstMatch = false;

	@Override
	public void renderWithOffset(World world, int x, int y, float offx, float offy) {

		if ((world.getMeta(x, y) & BlockFaces.UP) == BlockFaces.UP || world.getMeta(x, y) <= BlockFaces.NONE) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 0, true);
			if(findFirstMatch) return;
		}
		if ((world.getMeta(x, y) & BlockFaces.DOWN) == BlockFaces.DOWN) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 180, true);
			if(findFirstMatch) return;
		}
		if ((world.getMeta(x, y) & BlockFaces.LEFT) == BlockFaces.LEFT) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 270, true);
			if(findFirstMatch) return;
		}
		if ((world.getMeta(x, y) & BlockFaces.RIGHT) == BlockFaces.RIGHT) {
			Utils.drawRotated(
					world.batch,
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy), World.tilesizex, World.tilesizey, 90, true);
			if(findFirstMatch) return;
		}
	}
	
}
