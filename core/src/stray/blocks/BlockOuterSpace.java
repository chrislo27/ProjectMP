package stray.blocks;

import stray.Main;
import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.graphics.Texture;

public class BlockOuterSpace extends Block {

	public BlockOuterSpace(String path) {
		super(path);
	}

	public void renderPlain(Main main, float camerax, float cameray, int x, int y, int magic) {
		if (animationlink != null) {
			main.batch.draw(main.animations.get(animationlink).getCurrentFrame(), x
					* World.tilesizex - camerax, y * World.tilesizey - cameray, World.tilesizex,
					World.tilesizey);
			return;
		}

		if (usingMissingTex) {
			main.batch.draw(main.manager.get(AssetMap.get("blockmissingtexture"), Texture.class), x
					* World.tilesizex - camerax, y * World.tilesizey - cameray, World.tilesizex,
					World.tilesizey);
			return;
		}

		if (path == null) return;

		if (!connectedTextures) {
			if (!variants) {
				main.batch.draw(main.manager.get(sprites.get("defaulttex"), Texture.class), x
						* World.tilesizex - camerax, Main.convertY((y * World.tilesizey - cameray) + World.tilesizey));
			} else {
				main.batch.draw(main.manager.get(sprites.get("defaulttex"
						+ ((variantNum(magic, x, y)) & (varianttypes - 1))), Texture.class), x
						* World.tilesizex - camerax, Main.convertY((y * World.tilesizey - cameray) + World.tilesizey));
			}
		} else {
			main.batch.draw(main.manager.get(sprites.get("full"), Texture.class), x
					* World.tilesizex - camerax, Main.convertY((y * World.tilesizey - cameray) + World.tilesizey));

		}
	}

}
