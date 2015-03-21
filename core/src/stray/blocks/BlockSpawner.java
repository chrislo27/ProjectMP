package stray.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import stray.LevelEditor;
import stray.Main;
import stray.entity.Entity;
import stray.util.AssetMap;
import stray.world.World;

public abstract class BlockSpawner extends Block {

	public BlockSpawner(String path) {
		super(path);
		this.levelEditorGroup = LevelEditor.EditorGroup.SPAWNER;
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (world.getMeta(x, y) != 0) return;
		Entity e = getEntity(world, x, y);
		world.entities.add(e);

		world.setMeta(1, x, y);
	}

	@Override
	public void render(World world, int x, int y) {
		if (world.main.getScreen() != null) if ((world.main.getScreen() == Main.LEVELEDITOR)) {
			world.batch.setColor(MathUtils.random(), MathUtils.random(), MathUtils.random(), 0.2f);
			world.main.fillRect(x * world.tilesizex - world.camera.camerax,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey),
					World.tilesizex, World.tilesizey);
			world.batch.setColor(1, 1, 1, 1);

			if (path == null) return;

			world.batch.draw(
					world.main.manager.get(sprites.get("defaulttex"), Texture.class),
					x * world.tilesizex - world.camera.camerax,
					Main.convertY((y * world.tilesizey - world.camera.cameray)
							+ (world.main.manager.get(sprites.get("defaulttex"), Texture.class)
									.getHeight())));

		}
	}

	public abstract Entity getEntity(World world, int x, int y);

}
