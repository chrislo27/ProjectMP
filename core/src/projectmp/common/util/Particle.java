package projectmp.common.util;

import projectmp.common.Main;
import projectmp.common.block.Block.BlockFaces;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Particle implements Poolable {

	float x = 0;
	float y = 0;
	float velox = 0;
	float veloy = 0;
	float gravity = 0;
	public float lifetime = -1;
	private float startTime = -1;
	float prelife = 0;
	boolean destroyOnBlock = false;

	boolean clockwise = true;
	float rotspeed = 0;

	float tintr = 1;
	float tintg = 1;
	float tintb = 1;
	float tinta = 1;

	float startScale = 1f;
	float endScale = 1f;

	float initialRotation = 0;

	String texture = "poof";

	public Particle() {

	}

	public Particle setVelocity(float x, float y) {
		velox = x;
		veloy = y;
		return this;
	}

	public Particle setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * 
	 * @param t if it starts with _ it will render text
	 * @return
	 */
	public Particle setTexture(String t) {
		texture = t;
		return this;
	}

	public Particle setLifetime(float sec) {
		lifetime = sec;
		startTime = sec;
		return this;
	}

	public Particle setPrelife(float sec) {
		prelife = sec;
		return this;
	}

	public Particle setTint(Color c) {
		tintr = c.r;
		tintg = c.g;
		tintb = c.b;
		tinta = c.a;
		return this;
	}

	public Particle setTint(float r, float g, float b, float a) {
		tintr = r;
		tintg = g;
		tintb = b;
		tinta = a;
		return this;
	}

	public Particle setAlpha(float a) {
		tinta = a;
		return this;
	}

	public Particle setDestroyOnBlock(boolean b) {
		destroyOnBlock = b;
		return this;
	}

	public Particle setRotation(float seconds, boolean clockwise) {
		rotspeed = seconds;
		this.clockwise = clockwise;
		return this;
	}

	public Particle setStartScale(float sc) {
		startScale = sc;
		return this;
	}

	public Particle setEndScale(float end) {
		endScale = end;
		return this;
	}

	public Particle setGravity(float gr) {
		gravity = gr;
		return this;
	}

	public Particle setTotalScale(float sc) {
		return setStartScale(sc).setEndScale(sc);
	}

	public Particle setInitRotation(float r) {
		initialRotation = r;
		return this;
	}

	@Override
	public void reset() {
		x = 0;
		y = 0;
		velox = 0;
		veloy = 0;
		lifetime = -1;
		startTime = -1;
		prelife = 0;
		texture = "poof";
		destroyOnBlock = false;
		clockwise = true;
		rotspeed = 0;
		setTint(Color.WHITE);
		startScale = 1f;
		endScale = 1f;
		gravity = 0;
		initialRotation = 0;
	}

	private float getModifiedScale() {
		return (((startTime - lifetime) / startTime) * (endScale - startScale)) + startScale;
	}

	public void render(World world, Main main) {
		if (prelife <= 0) {
			update(Gdx.graphics.getDeltaTime());

			if (texture != null) {
				if (texture.startsWith("_")) {
					main.font.setColor(tintr, tintg, tintb,
							(lifetime <= 0.1f ? (Math.min(lifetime * 10f, tinta)) : tinta));
					main.drawCentered(main.font, texture.substring(1), x * World.tilesizex,
							Main.convertY(y * World.tilesizey));
					main.font.setColor(Color.WHITE);
				} else {
					Texture t = AssetRegistry.getTexture(texture.startsWith("real-") ? texture
							.substring(5) : texture);

					main.batch.setColor(tintr, tintg, tintb,
							(lifetime <= 0.1f ? (Math.min(lifetime * 10f, tinta)) : tinta));

					Utils.drawRotatedCentered(
							main.batch,
							t,
							x * World.tilesizex,
							Main.convertY(y * World.tilesizey),
							t.getWidth() * getModifiedScale(),
							t.getHeight() * getModifiedScale(),
							((rotspeed > 0 ? (MathHelper.getNumberFromTime(rotspeed) * 360) : 0) + initialRotation) % 360,
							clockwise);

					main.batch.setColor(Color.WHITE);
				}
			}

		}

		if (prelife > 0) {
			prelife -= Gdx.graphics.getDeltaTime();
		} else if (prelife <= 0) {
			lifetime -= Gdx.graphics.getDeltaTime();
		}

		if (destroyOnBlock) {
			if ((world.getBlock((int) x, (int) y).isSolid(world, (int) x, (int) y) != BlockFaces.NONE)) if (MathHelper
					.intersects((int) x, (int) y, 1, 1, x - (4 * World.tilepartx), y
							- (4 * World.tileparty), 8 * World.tilepartx, 8 * World.tileparty)) {
				lifetime = -1;
				prelife = -1;
			}
		}

	}

	private void update(float delta) {
		x += velox * delta;
		y += veloy * delta;
		veloy += gravity * delta;
	}

}
