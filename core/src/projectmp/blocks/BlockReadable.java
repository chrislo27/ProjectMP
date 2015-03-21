package projectmp.blocks;

import java.util.HashMap;

import projectmp.Main;
import projectmp.Translator;
import projectmp.conversation.Conversations;
import projectmp.world.MetaStrings;
import projectmp.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

public class BlockReadable extends Block {

	public BlockReadable(String path) {
		super(path);
	}

	@Override
	public void render(World world, int x, int y) {
		super.render(world, x, y);
		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			world.main.font.setColor(Color.WHITE);
			world.main.drawCentered(Translator.getMsg("block.readable"), x * world.tilesizex
					- world.camera.camerax + (World.tilesizex / 2),
					Main.convertY((y * world.tilesizey - world.camera.cameray) - 15));
			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				if (world.main.getConv() == null) {
					if (world.getMeta(x, y) != 0) {
						if (Conversations.instance().convs.containsKey(MetaStrings.instance().map
								.get(world.getMeta(x, y)))) {
							onRead(world, x, y);
							world.main.setConv(Conversations.instance().convs.get(MetaStrings
									.instance().map.get(world.getMeta(x, y))));
						}
					}
				}
			}
		}
	}

	public void onRead(World world, int x, int y) {

	}

}
