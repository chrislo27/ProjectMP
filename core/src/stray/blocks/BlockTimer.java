package stray.blocks;

import stray.LevelEditor;
import stray.Main;
import stray.Translator;
import stray.conversation.Conversations;
import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class BlockTimer extends Block implements AffectsColour {

	public BlockTimer(Color c, String col) {
		super(null);
		switchColour = col;
		renderColour = c;
		levelEditorGroup = LevelEditor.EditorGroup.TIMER;
	}

	String switchColour = "";
	Color renderColour = Color.WHITE;

	public static final float SECONDS = 5f;

	@Override
	public void renderWithOffset(World world, int x, int y, float offx, float offy) {
		world.batch.draw(
				world.main.textures.get("timer"), x
						* world.tilesizex - world.camera.camerax + offx,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey + offy));
		world.batch.setColor(renderColour);
		world.batch.draw(
				world.main.textures.get("timer_colour"), x
						* world.tilesizex - world.camera.camerax + offx,
				Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey + offy));
		world.batch.setColor(1, 1, 1, 1);
		
		world.main.font.setColor(Color.WHITE);
		if (world.main.getScreen() == Main.LEVELEDITOR) return;
		if (world.getMeta(x, y) != 0) {
			world.main.drawCentered(String.format("%.1f",
					((world.getMeta(x, y) * 1f) / Main.TICKS)), x
					* world.tilesizex - world.camera.camerax + (World.tilesizex / 2) + offx, Main
					.convertY((y * world.tilesizey - world.camera.cameray) - 30 + offy));
		}
		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			world.main.drawCentered(Translator.getMsg("block.triggertimer"), x * world.tilesizex
					- world.camera.camerax + (World.tilesizex / 2) + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) - 15 + offy));

			if (Gdx.input.isKeyJustPressed(Keys.UP)) {
				world.setMeta(Math.round(SECONDS * Main.TICKS), x, y);
			}
		}
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (world.getMeta(x, y) != 0) {
			int i = world.getMeta(x, y);

			if (i <= 0) {
				world.setMeta(0, x, y);
				if (world.global.getInt(switchColour) != 0 && !BlockSwitch.areOtherBlocksOn(world, x, y, switchColour)) {
					world.global.setInt(switchColour, 0);
					Block.playSound(x, y, world.camera.camerax, world.camera.cameray,
							world.main.manager.get(AssetMap.get("switchsfx"), Sound.class), 1, 0.6f, false);
				}
			} else {
				if (i % ((int) Main.TICKS) == 0) {
					Block.playSound(x, y, world.camera.camerax, world.camera.cameray,
							world.main.manager.get(AssetMap.get("switchsfx"), Sound.class), 1, 0.8f, false);
				} else if ((i + ((int) Main.TICKS) / 3f) % ((int) Main.TICKS) == 0) {
					Block.playSound(x, y, world.camera.camerax, world.camera.cameray,
							world.main.manager.get(AssetMap.get("switchsfx"), Sound.class), 1, 1f, false);
				}

				if (world.global.getInt(switchColour) != 1) {
					world.global.setInt(switchColour, 1);
				}
			}

			i--;
			if (world.getMeta(x, y) != 0) world.setMeta(i, x, y);
		}
	}

	@Override
	public boolean colourOn(World world, int x, int y) {
		return world.getMeta(x, y) != 0;
	}

	@Override
	public String getColour(World world, int x, int y) {
		return switchColour;
	}
}
