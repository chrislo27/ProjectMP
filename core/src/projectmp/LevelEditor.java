package projectmp;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import projectmp.blocks.Block;
import projectmp.blocks.Blocks;
import projectmp.blocks.Block.BlockFaces;
import projectmp.entity.Entity;
import projectmp.world.LevelEditorWorld;
import projectmp.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class LevelEditor extends Updateable {

	public LevelEditor(Main m) {
		super(m);
		Iterator it = Blocks.instance().getAllBlocks();
		while (it.hasNext()) {
			String b = ((Entry<String, Block>) it.next()).getKey();
			if (Blocks.instance().getBlock(b).levelEditorGroup == null) {
				continue;
			}
			blocks.add(b);
		}
		// blocks.sort();

		iothread = new Thread() {

			public void run() {
				while (true) {
					try {
						this.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (iothreadtodo != 0) {
						if (iothreadtodo == 1) {
							JFileChooser fileChooser = new JFileChooser();
							if (lastFile != null) {
								fileChooser.setCurrentDirectory(lastFile);
							} else {
								fileChooser.setCurrentDirectory(new File(System
										.getProperty("user.home"), "Desktop"));
							}
							fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
							fileChooser.setSelectedFile(new File("a-custom-level.xml"));
							fileChooser.setDialogTitle("Select a directory to save in...");
							int result = fileChooser.showSaveDialog(null);
							if (result == JFileChooser.APPROVE_OPTION) {
								File selectedFile = fileChooser.getSelectedFile();
								lastFile = selectedFile;
								world.save(new FileHandle(selectedFile));
							}
						} else if (iothreadtodo == 2) {
							JFileChooser fileChooser = new JFileChooser();
							if (lastFile != null) {
								fileChooser.setCurrentDirectory(lastFile);
							} else {
								fileChooser.setCurrentDirectory(new File(System
										.getProperty("user.home"), "Desktop"));
							}
							fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
							fileChooser.setDialogTitle("Open an .xml file");
							fileChooser.setFileFilter(new FileNameExtensionFilter(".xml Files",
									"xml"));

							int result = fileChooser.showOpenDialog(null);

							if (result == JFileChooser.APPROVE_OPTION) {
								File selectedFile = fileChooser.getSelectedFile();
								lastFile = selectedFile;
								world.load(new FileHandle(selectedFile));
								if (world.getPlayer() != null) world.camera.forceCenterOn(
										world.getPlayer().x, world.getPlayer().y);
								world.entities.clear();
							}
						}

						iothreadtodo = 0;
					}
				}
			}
		};
		iothread.setDaemon(true);
		iothread.start();
	}

	Array<String> blocks = new Array<String>();

	private File lastFile = null;

	private Thread iothread;
	/**
	 * 0 = nothing, 1 = save, 2 = load
	 */
	private int iothreadtodo = 0;

	int selx = 0;
	int sely = 0;

	int blocksel = 0;
	public int defaultmeta = 0;

	LevelEditorWorld world;

	public void resetWorld() {
		if (world == null) {
			world = new LevelEditorWorld(main);
		}
		world.renderer.showGrid = true;
		world.prepare();
		if (world.getPlayer() != null) {
			world.setBlock(Blocks.instance().getBlock("spawnerplayer"), (int) world.getPlayer().x,
					(int) world.getPlayer().y);
		}
		world.entities.clear();
	}

	@Override
	public void render(float delta) {
		world.renderOnly();

		selx = world.getRoomX(Main.getInputX());
		sely = world.getRoomY(Main.getInputY());

		main.batch.begin();

		main.batch.setColor(1, 1, 1, 0.5f);
		Blocks.instance()
				.getBlock(blocks.get(blocksel))
				.renderWithOffset(world, -1337, -1337, ((selx + 1337) * World.tilesizex),
						((sely + 1337) * World.tilesizey));
		main.batch.setColor(1, 1, 1, 1);

		if (Gdx.input.isKeyPressed(Keys.TAB)) {
			renderPalette();
		}

		main.drawInverse("DEBUG MODE RECOMMENDED - F12", Settings.DEFAULT_WIDTH - 5,
				Gdx.graphics.getHeight() - 5);
		main.drawInverse("ALT+S - save", Settings.DEFAULT_WIDTH - 5, Gdx.graphics.getHeight() - 20);
		main.drawInverse("ALT+O - open", Settings.DEFAULT_WIDTH - 5, Gdx.graphics.getHeight() - 35);
		main.drawInverse("NUMPAD 8462 - change level dimensions (will reset level!)",
				Settings.DEFAULT_WIDTH - 5, Gdx.graphics.getHeight() - 50);
		main.drawInverse("ALT+T - test level", Settings.DEFAULT_WIDTH - 5,
				Gdx.graphics.getHeight() - 65);
		main.drawInverse("IJKL - XOR bit, O to reset, hold SHIFT to overlap",
				Settings.DEFAULT_WIDTH - 5, Gdx.graphics.getHeight() - 80);
		main.drawInverse("hold TAB - block picker", Settings.DEFAULT_WIDTH - 5,
				Gdx.graphics.getHeight() - 95);
		main.drawInverse("ALT+N - force save in new file", Settings.DEFAULT_WIDTH - 5,
				Gdx.graphics.getHeight() - 110);

		main.batch.end();

		world.camera.clamp();
	}

	private void renderPalette() {
		for (int i = 0; i < EditorGroup.values().length; i++) {
			main.batch.setColor(0, 0, 0, 0.5f);
			main.fillRect(0, i * 64, Settings.DEFAULT_WIDTH, 64);
			main.batch.setColor(1, 1, 1, 1);

			int blockiter = 0;
			for (String b : blocks) {
				if (Blocks.instance().getBlock(b).levelEditorGroup == EditorGroup.values()[i]) {
					Blocks.instance()
							.getBlock(b)
							.renderWithOffset(
									world,
									0,
									0,
									world.camera.camerax + (blockiter * 64) + 64,
									world.camera.cameray + Settings.DEFAULT_HEIGHT
											- World.tilesizey - (i * 64));

					blockiter++;
				}
			}

			main.font.setScale(2.5f);
			main.drawCentered("" + i, 32, i * 64 - 8 + 64);
			main.font.setScale(1);
			main.drawScaled(EditorGroup.values()[i].name(), 32, i * 64 - 48 + 64, 64, 2);
			main.font.setScale(1);
			main.fillRect(0, i * 64 + 62, Settings.DEFAULT_WIDTH, 2);
			main.fillRect(64, i * 64, 2, 64);
		}

		main.batch.setColor(1, 1, 1, 1);

		for (int i = 0; i < EditorGroup.values().length; i++) {

			int blockiter = 0;
			for (String b : blocks) {
				if (Blocks.instance().getBlock(b).levelEditorGroup == EditorGroup.values()[i]) {
					if (Main.getInputX() >= (blockiter * 64) + World.tilesizex
							&& Main.getInputX() <= (blockiter * 64) + (World.tilesizex * 2)) {
						if (Main.getInputY() >= Settings.DEFAULT_HEIGHT - World.tilesizey
								- (i * 64)
								&& Main.getInputY() <= Settings.DEFAULT_HEIGHT - World.tilesizey
										- (i * 64) + World.tilesizey) {
							if (Gdx.input.isButtonPressed(Buttons.LEFT)) blocksel = Math.max(0,
									blocks.lastIndexOf(b, false));
							main.drawTextBg(b, Main.getInputX(), Main.convertY(Main.getInputY()));
						}
					}

					blockiter++;
				}
			}
		}
	}

	private void save() {
		for (Entity e : world.entities) {
			if (e == world.getPlayer()) {
				world.entities.removeValue(e, true);
				break;
			}
		}
		if (lastFile != null) {
			world.save(new FileHandle(lastFile));
		} else {
			iothreadtodo = 1;
		}
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
		main.font.draw(main.batch, "selx: " + selx, 5, Main.convertY(starting));
		main.font.draw(main.batch, "sely: " + sely, 5, Main.convertY(starting + 15));
		main.font.draw(main.batch, "block at " + selx + ", " + sely + ": "
				+ Blocks.instance().getKey(world.getBlock(selx, sely)), 5,
				Main.convertY(starting + 30));
		main.font.draw(main.batch, "block selected: " + blocks.get(blocksel), 5,
				Main.convertY(starting + 60));
		main.font.draw(main.batch, "default meta: " + defaultmeta + ", "
				+ getOrientationsFromMeta(), 5, Main.convertY(starting + 75));
		main.font.draw(main.batch, "block meta: " + world.getMeta(selx, sely), 5,
				Main.convertY(starting + 90));
		main.font.draw(main.batch, "world sizex: " + world.sizex, 5, Main.convertY(starting + 120));
		main.font.draw(main.batch, "world sizey: " + world.sizey, 5, Main.convertY(starting + 135));
		main.font.draw(main.batch,
				"file location: " + (lastFile == null ? null : lastFile.getName()), 5,
				Main.convertY(starting + 165));
	}

	private String getOrientationsFromMeta() {
		return "" + ((defaultmeta & BlockFaces.UP) == BlockFaces.UP ? "I" : "")
				+ ((defaultmeta & BlockFaces.LEFT) == BlockFaces.LEFT ? "J" : "")
				+ ((defaultmeta & BlockFaces.DOWN) == BlockFaces.DOWN ? "K" : "")
				+ ((defaultmeta & BlockFaces.RIGHT) == BlockFaces.RIGHT ? "L" : "");
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		if (world == null) {
			resetWorld();
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			world.camera.cameray -= (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) ? 32 : 16)
					* (Gdx.graphics.getDeltaTime() * World.tilesizey);
			world.camera.clamp();
		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))
				&& !(Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input
						.isKeyPressed(Keys.ALT_RIGHT))) {
			world.camera.cameray += (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) ? 32 : 16)
					* (Gdx.graphics.getDeltaTime() * World.tilesizey);
			world.camera.clamp();
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			world.camera.camerax -= (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) ? 32 : 16)
					* (Gdx.graphics.getDeltaTime() * World.tilesizex);
			world.camera.clamp();
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			world.camera.camerax += (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) ? 32 : 16)
					* (Gdx.graphics.getDeltaTime() * World.tilesizex);
			world.camera.clamp();
		}

		if (Gdx.input.isKeyJustPressed(Keys.I)) {
			if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					&& !Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
				defaultmeta = 0;
			}
			defaultmeta ^= BlockFaces.UP;
		} else if (Gdx.input.isKeyJustPressed(Keys.K)) {
			if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					&& !Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
				defaultmeta = 0;
			}
			defaultmeta ^= BlockFaces.DOWN;
		}
		if (Gdx.input.isKeyJustPressed(Keys.J)) {
			if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					&& !Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
				defaultmeta = 0;
			}
			defaultmeta ^= BlockFaces.LEFT;
		} else if (Gdx.input.isKeyJustPressed(Keys.L)) {
			if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					&& !Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
				defaultmeta = 0;
			}
			defaultmeta ^= BlockFaces.RIGHT;
		}
		if (Gdx.input.isKeyJustPressed(Keys.O)) {
			defaultmeta = 0;
		}

		if (!Gdx.input.isKeyPressed(Keys.TAB)) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				world.setBlock(Blocks.instance().getBlock(blocks.get(blocksel)), selx, sely);
				world.setMeta(defaultmeta, selx, sely);
			} else if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
				world.setBlock(Blocks.instance().getBlock(Blocks.defaultBlock), selx, sely);
				world.setMeta(0, selx, sely);
			} else if (Gdx.input.isButtonPressed(Buttons.MIDDLE)) {
				if (!Blocks.instance().getKey(world.getBlock(selx, sely))
						.equals(blocks.get(blocksel))) {
					for (int i = 0; i < blocks.size; i++) {
						if (blocks.get(i).equals(
								Blocks.instance().getKey(world.getBlock(selx, sely)))) {
							blocksel = i;
							break;
						}
					}
				}
				defaultmeta = world.getMeta(selx, sely);
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.setScreen(Main.MAINMENU);
		}

		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_4)) {
			if (world.sizex > 30) world.sizex -= 2;
			resetWorld();
		} else if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_6)) {
			if (world.sizex < 80) world.sizex += 2;
			resetWorld();
		}

		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_8)) {
			if (world.sizey < 60) world.sizey += 1;
			resetWorld();
		} else if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_2)) {
			if (world.sizey > 20) world.sizey -= 2;
			resetWorld();
		}

		if (Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT)) {
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
				if (Gdx.input.isKeyJustPressed(Keys.S)) {
					iothreadtodo = 1;
				}
			} else {
				if (Gdx.input.isKeyJustPressed(Keys.O)) {
					iothreadtodo = 2;
				} else if (Gdx.input.isKeyJustPressed(Keys.S)) {
					save();
				} else if (Gdx.input.isKeyJustPressed(Keys.T)) {
					if (lastFile != null) {
						Main.TESTLEVEL.world.load(new FileHandle(lastFile));
						main.setScreen(Main.TESTLEVEL);
					}
				} else if (Gdx.input.isKeyJustPressed(Keys.N)) {
					lastFile = null;
				}
			}
		}
	}

	public static enum EditorGroup {

		NORMAL, HAZARD, TOGGLE, BUTTON, TIMER, SPAWNER, COLLECT;

	}
}
