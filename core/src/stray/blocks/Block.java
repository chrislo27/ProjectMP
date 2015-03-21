package stray.blocks;

import java.util.HashMap;

import stray.LevelEditor.EditorGroup;
import stray.Main;
import stray.Settings;
import stray.entity.Entity;
import stray.util.AssetMap;
import stray.util.MathHelper;
import stray.util.Utils;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Block {

	/**
	 * 
	 * @param path
	 *            actual path; omit .png!
	 */
	public Block(String path) {
		this.path = path;
	}

	public static final int globalMagicNumber = Main.getRandom().nextInt();

	protected boolean connectedTextures = false;
	protected boolean variants = false;
	protected String animationlink = null;
	protected int varianttypes = 4;
	protected int solidFaces = BlockFaces.NONE;
	protected boolean usingMissingTex = false;

	public EditorGroup levelEditorGroup = EditorGroup.NORMAL;

	public void tickUpdate(World world, int x, int y) {

	}

	public float getDragCoefficient(World world, int x, int y) {
		return 1;
	}

	public boolean isRenderedFront() {
		return false;
	}

	public Block solidify(int faces) {
		solidFaces = faces;
		return this;
	}

	public void onCollideLeftFace(World world, int x, int y, Entity e) {

	}

	public void onCollideRightFace(World world, int x, int y, Entity e) {

	}

	public void onCollideUpFace(World world, int x, int y, Entity e) {

	}

	public void onCollideDownFace(World world, int x, int y, Entity e) {

	}

	public int isSolid(World world, int x, int y) {
		return solidFaces;
	}

	public String getAnimation() {
		return animationlink;
	}

	public Block setAnimation(String animationlink) {
		this.animationlink = animationlink;
		return this;
	}

	public void dispose() {

	}

	public int getTickRate() {
		return 1;
	}

	/**
	 * turns off connected textures too
	 * 
	 * @return itself for chaining
	 */
	public Block hasVariants(int types) {
		variants = true;
		varianttypes = types;
		connectedTextures = false;
		return this;
	}

	public Block useConTextures() {
		connectedTextures = true;

		return this;
	}

	public Block setEditorGroup(EditorGroup i) {
		levelEditorGroup = i;
		return this;
	}

	public void addTextures(Main main) {
		if (path == null) return;

		if (animationlink != null) {
			// assumes animation is loaded already
			return;
		}

		if (!connectedTextures) {
			if (!Gdx.files.internal(path + ".png").exists() && !variants) {
				Main.logger.warn("WARNING: a block has no texture (" + path
						+ ".png); using missing texture");
				connectedTextures = false;
				variants = false;
				usingMissingTex = true;

				return;
			}
		} else {
			if (!Gdx.files.internal(path + "-full.png").exists() && !variants) {
				Main.logger.warn("WARNING: a block has no \"full\" texture (" + path
						+ ".png); using missing texture");
				connectedTextures = false;
				variants = false;
				usingMissingTex = true;

				return;
			}
		}
		if (!connectedTextures) {
			if (!variants) {
				main.manager.load(path + ".png", Texture.class);
			} else {
				for (int i = 0; i < varianttypes; i++) {
					main.manager.load(path + i + ".png", Texture.class);
				}
			}
		} else {

			main.manager.load(path + "-full.png", Texture.class);
			main.manager.load(path + "-corner.png", Texture.class);
			main.manager.load(path + "-edgehor.png", Texture.class);
			main.manager.load(path + "-edgever.png", Texture.class);
		}

	}

	public void postLoad(Main main) {
		sprites = new HashMap<String, String>();
		if (animationlink != null) return;
		if (path == null) return;
		if (usingMissingTex) return;
		if (connectedTextures) {

			sprites.put("full", path + "-full.png");
			sprites.put("corner", path + "-corner.png");
			sprites.put("edgehor", path + "-edgehor.png");
			sprites.put("edgever", path + "-edgever.png");
		} else {
			if (!variants) {
				sprites.put("defaulttex", path + ".png");
			} else {
				for (int i = 0; i < varianttypes; i++) {
					sprites.put("defaulttex" + i, path + i + ".png");
				}
			}
		}
	}

	// protected void drawAt(Batch batch, Texture sprite, float f, float g) {
	// batch.draw(sprite, f, Main.convertY(g + World.tilesizey),
	// World.tilesizex, World.tilesizey);
	// }

	/**
	 * only used for connected textures connected: full, corner, edgehor,
	 * edgever
	 */
	protected HashMap<String, String> sprites;

	protected String path = "";

	private int getVarFromTime() {

		int time = (MathHelper.getNthDigit(System.currentTimeMillis(), 1))
				+ (MathHelper.getNthDigit(System.currentTimeMillis(), 2) * 10)
				+ (MathHelper.getNthDigit(System.currentTimeMillis(), 3) * 100)
				+ (MathHelper.getNthDigit(System.currentTimeMillis(), 4) * 1000);
		// 10k
		return ((int) time / (10000 / varianttypes));
	}

	// /**
	// * topleft origin!
	// *
	// * @param batch
	// * @param x
	// * @param y
	// */
	// public void renderModel(World world, int x, int y) {
	// Batch batch = world.batch;
	// if (path == null) return;
	// if (usingMissingTex) {
	// batch.draw(world.main.manager.get(AssetMap.get("blockmissingtexture"),
	// Texture.class),
	// x, Main.convertY(y + World.tilesizey), World.tilesizex, World.tilesizey);
	// return;
	// }
	//
	// if (animationlink != null) {
	// batch.draw(world.main.animations.get(animationlink).getCurrentFrame(), x,
	// Main.convertY(y + World.tilesizey), World.tilesizex, World.tilesizey);
	// return;
	// }
	//
	// if (!connectedTextures) {
	// if (!variants) {
	// drawAt(batch, world.main.manager.get(sprites.get("defaulttex"),
	// Texture.class), x,
	// y);
	// } else {
	// drawAt(batch, world.main.manager.get(sprites.get("defaulttex" +
	// getVarFromTime()),
	// Texture.class), x, y);
	// }
	// } else {
	// drawAt(batch, world.main.manager.get(sprites.get("full"), Texture.class),
	// x, y);
	// }
	// }

	public static int variantNum(World world, int x, int y) {
		return variantNum(world.msTime, x, y);
	}

	public static int variantNum(long magic, int x, int y) {
		return ((int) ((magic + (x + 17) * (y + 53) * 214013L + 2531011L) >> 16) & 0x7fff);
	}

	public static int variantNum(int x, int y) {
		return variantNum(Block.globalMagicNumber, x, y);
	}

	// public void renderPlain(Main main, float camerax, float cameray, int x,
	// int y, int magic) {
	// if (animationlink != null) {
	// main.batch.draw(main.animations.get(animationlink).getCurrentFrame(), x
	// * World.tilesizex - camerax, y * World.tilesizey - cameray,
	// World.tilesizex,
	// World.tilesizey);
	// return;
	// }
	//
	// if (usingMissingTex) {
	// main.batch.draw(main.manager.get(AssetMap.get("blockmissingtexture"),
	// Texture.class), x
	// * World.tilesizex - camerax, y * World.tilesizey - cameray,
	// World.tilesizex,
	// World.tilesizey);
	// return;
	// }
	//
	// if (path == null) return;
	//
	// if (!connectedTextures) {
	// if (!variants) {
	// drawAt(main.batch, main.manager.get(sprites.get("defaulttex"),
	// Texture.class), x
	// * World.tilesizex - camerax, y * World.tilesizey - cameray);
	// } else {
	// drawAt(main.batch, main.manager.get(sprites.get("defaulttex"
	// + ((variantNum(magic, x, y)) & (varianttypes - 1))), Texture.class), x
	// * World.tilesizex - camerax, y * World.tilesizey - cameray);
	// }
	// } else {
	// drawAt(main.batch, main.manager.get(sprites.get("full"), Texture.class),
	// x
	// * World.tilesizex - camerax, y * World.tilesizey - cameray);
	//
	// }
	// }

	public void render(World world, int x, int y) {
		renderWithOffset(world, x, y, 0, 0);
	}

	public void renderWithOffset(World world, int x, int y, float offx, float offy) {
		if (usingMissingTex) {
			world.batch.draw(
					world.main.manager.get(AssetMap.get("blockmissingtexture"), Texture.class),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
			return;
		}
		if (animationlink != null) {
			world.batch.draw(
					world.main.animations.get(animationlink).getCurrentFrame(),
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
			return;
		}
		if (path == null) return;

		if (!connectedTextures) {
			if (!variants) {
				world.batch.draw(
						world.main.manager.get(sprites.get("defaulttex"), Texture.class),
						x * world.tilesizex - world.camera.camerax + offx,
						Main.convertY((y * world.tilesizey - world.camera.cameray)
								+ World.tilesizey + offy));
			} else {
				world.batch.draw(world.main.manager.get(sprites.get("defaulttex"
						+ ((variantNum(world, x, y)) & (varianttypes - 1))), Texture.class), x
						* world.tilesizex - world.camera.camerax + offx, Main.convertY((y
						* world.tilesizey - world.camera.cameray)
						+ World.tilesizey + offy));
			}
		} else {
			drawConnectedTexture(world, x, y, offx, offy,
					world.main.manager.get(sprites.get("corner"), Texture.class),
					world.main.manager.get(sprites.get("full"), Texture.class),
					world.main.manager.get(sprites.get("edgever"), Texture.class),
					world.main.manager.get(sprites.get("edgehor"), Texture.class));
		}
	}

	public void drawConnectedTexture(World world, int x, int y, float offx, float offy,
			Texture corner, Texture full, Texture ver, Texture hor) {
		boolean up, down, left, right;
		up = world.getBlock(x, y - 1) == this;
		down = world.getBlock(x, y + 1) == this;
		left = world.getBlock(x - 1, y) == this;
		right = world.getBlock(x + 1, y) == this;

		if (up && down && left && right) {
			if (world.getBlock(x + 1, y + 1) != this || world.getBlock(x + 1, y - 1) != this
					|| world.getBlock(x - 1, y + 1) != this || world.getBlock(x - 1, y - 1) != this) {
				world.batch.draw(
						corner,
						x * world.tilesizex - world.camera.camerax + offx,
						Main.convertY((y * world.tilesizey - world.camera.cameray)
								+ World.tilesizey + offy));
			} else world.batch.draw(
					full,
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
		} else if (up && down && (left == false || right == false)) {
			world.batch.draw(
					ver,
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
		} else if (left && right && (up == false || down == false)) {
			world.batch.draw(
					hor,
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
		} else {
			world.batch.draw(
					corner,
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
		}
	}

	public void drawConnectedTexture(World world, int x, int y, float offx, float offy,
			TextureRegion corner, TextureRegion full, TextureRegion ver, TextureRegion hor) {
		boolean up, down, left, right;
		up = world.getBlock(x, y - 1) == this;
		down = world.getBlock(x, y + 1) == this;
		left = world.getBlock(x - 1, y) == this;
		right = world.getBlock(x + 1, y) == this;

		if (up && down && left && right) {
			if (world.getBlock(x + 1, y + 1) != this || world.getBlock(x + 1, y - 1) != this
					|| world.getBlock(x - 1, y + 1) != this || world.getBlock(x - 1, y - 1) != this) {
				world.batch.draw(
						corner,
						x * world.tilesizex - world.camera.camerax + offx,
						Main.convertY((y * world.tilesizey - world.camera.cameray)
								+ World.tilesizey));
			} else world.batch.draw(
					full,
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
		} else if (up && down && (left == false || right == false)) {
			world.batch.draw(
					ver,
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
		} else if (left && right && (up == false || down == false)) {
			world.batch.draw(
					hor,
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
		} else {
			world.batch.draw(
					corner,
					x * world.tilesizex - world.camera.camerax + offx,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey
							+ offy));
		}
	}

	// protected void drawAt(SpriteBatch batch, TextureRegion corner, float f,
	// float g) {
	// batch.draw(corner, f, Main.convertY(g + World.tilesizey),
	// World.tilesizex, World.tilesizey);
	// }

	public static boolean isEntityNear(World world, int x, int y, int rad, Class<?> cls) {
		for (int i = 0; i < world.entities.size; i++) {
			Entity e = world.entities.get(i);
			if (!(cls.isInstance(e))) continue;
			if (MathHelper.intersects(e.x, e.y, e.sizex, e.sizey, x - rad + 1, y - rad + 1,
					rad * 2 - 1, rad * 2 - 1)) return true;
		}

		return false;
	}

	public static Entity getNearestEntity(World world, int x, int y, int rad, Class<?> cls) {
		if (isEntityNear(world, x, y, rad, cls)) {
			for (int i = 0; i < world.entities.size; i++) {
				Entity e = world.entities.get(i);
				if (!(cls.isInstance(e))) continue;
				if (MathHelper.intersects(e.x, e.y, e.sizex, e.sizey, x - rad + 1, y - rad + 1,
						rad * 2 - 1, rad * 2 - 1)) return e;
			}
		}

		return null;
	}

	public static boolean entityIntersects(World world, double x, double y, Entity e) {
		return entityIntersects(world, x, y, e, 1, 1);
	}

	public static boolean entityIntersects(World world, double x, double y, Entity e, double sizex,
			double sizey) {
		if (e == null) return false;
		return MathHelper.intersects(x, y, sizex, sizey, e.x, e.y, e.sizex, e.sizey);
	}

	public static boolean isBlockVisible(float camx, float camy, int x, int y) {
		return MathHelper.intersects(x * World.tilesizex, y * World.tilesizey, World.tilesizex,
				World.tilesizey, camx, camy, Settings.DEFAULT_WIDTH, Gdx.graphics.getHeight());
	}

	public static boolean playSound(int x, int y, float camx, float camy, Sound sound, float vol,
			float pitch, boolean mustbevisible) {
		if (!Block.isBlockVisible(camx, camy, x, y) && mustbevisible) return false;
		sound.play(vol * Settings.soundVolume, pitch, Utils.getSoundPan(x, camx));
		return true;
	}

	public static boolean playSound(int x, int y, float camx, float camy, Sound sound, float vol,
			float pitch) {
		return playSound(x, y, camx, camy, sound, vol, pitch, true);
	}

	public static class BlockFaces {

		public static final int NONE = 0x0;
		public static final int ALL = 0xF;
		public static final int UP = 0x1;
		public static final int DOWN = 0x2;
		public static final int LEFT = 0x4;
		public static final int RIGHT = 0x8;

	}

}
