package projectmp.common.entity;

import projectmp.client.WorldRenderer;
import projectmp.common.Main;
import projectmp.common.block.Block.BlockFaces;
import projectmp.common.block.BlockEmpty;
import projectmp.common.io.CanBeSavedToNBT;
import projectmp.common.util.Coordinate;
import projectmp.common.util.MathHelper;
import projectmp.common.util.Sizeable;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evilco.mc.nbt.error.TagNotFoundException;
import com.evilco.mc.nbt.error.UnexpectedTagTypeException;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagFloat;
import com.evilco.mc.nbt.tag.TagLong;

public abstract class Entity implements Sizeable, CanBeSavedToNBT{

	public transient World world;
	public float x = 0;
	public float y = 0;
	public float visualX = x;
	public float visualY = y;
	public transient float lastPacketX = x;
	public transient float lastPacketY = y;
	public transient float lastTickX = x;
	public transient float lastTickY = y;
	private transient boolean shouldPredictFuture = false;
	public float sizex = 1;
	public float sizey = 1;
	public float velox = 0;
	public float veloy = 0;
	
	public long uuid = MathUtils.random(Long.MIN_VALUE, Long.MAX_VALUE);
	
	/**
	 * collides with blocks
	 */
	public boolean hasBlockCollision = true;

	/**
	 * collides with other entities with this flag as true
	 */
	public boolean hasEntityCollision = false;

	/**
	 * indicates that entities that hit this other entity will shift
	 * velocity*forceTransfer to this entity (and stop)
	 */
	public float forceTransfer = 0f;
	
	public float dragCoefficient = 1;
	public float gravityCoefficient = 1;
	public float bounceCoefficient = 0;
	
	public float accspeed = 1.5f; // acceleration blocks/sec
	public float maxspeed = 1.5f; // speed cap blocks/sec
	
	/**
	 * used for deserialization
	 */
	public Entity(){
		prepare();
	}
	
	public Entity(World w, float posx, float posy) {
		world = w;
		x = posx;
		y = posy;
		visualX = x;
		visualY = y;
		lastPacketX = x;
		lastPacketY = y;
		if(world != null) uuid = world.getNewUniqueUUID();
		prepare();
	}
	
	/**
	 * called on creation
	 */
	public abstract void prepare();
	
	/**
	 * you MUST adhere to the batch's color!
	 * @param renderer
	 */
	public abstract void render(WorldRenderer renderer);
	
	/**
	 * called every render update BEFORE rendering on client only
	 */
	public void clientRenderUpdate() {
		visualX += ((x - visualX) * Gdx.graphics.getDeltaTime() * Main.TICKS);
		visualY += ((y - visualY) * Gdx.graphics.getDeltaTime() * Main.TICKS);

		if (Math.abs(x - visualX) <= World.tilepartx) visualX = x;
		if (Math.abs(y - visualY) <= World.tileparty) visualY = y;

		if (visualX == x && visualY == y) shouldPredictFuture = true;
		if (shouldPredictFuture) {
			visualX += (velox * Gdx.graphics.getDeltaTime() / Main.TICKS);
			visualY += (veloy * Gdx.graphics.getDeltaTime() / Main.TICKS);
		}
	}

	public void positionUpdate(float newx, float newy){
		lastPacketX = x;
		lastPacketY = y;
		x = newx;
		y = newy;
		shouldPredictFuture = false;
	}
	
	public boolean hasMovedLastTick(){
		if(lastTickX == x && lastTickY == y) return false;
		
		return true;
	}
	
	/**
	 * called every tick, before rendering
	 */
	public void tickUpdate() {
		movementAndCollision();
	}
	
	public void movementAndCollision(){
		lastTickX = x;
		lastTickY = y;
		
		float drag = world.drag * getLowestDrag() * dragCoefficient;
		if (velox > 0) {
			velox -= drag / Main.TICKS;
			if (velox < 0) velox = 0;
		} else if (velox < 0) {
			velox += drag / Main.TICKS;
			if (velox > 0) velox = 0;
		}

		if (getBlockCollidingDown() == null && getEntityCollidingDown() == null) {
			veloy += (world.gravity / Main.TICKS) * gravityCoefficient;
		}

		if (veloy != 0) {
			int velo = (int) (veloy / Main.TICKS * World.tilesizey);

			if (velo > 0) {
				Coordinate c = getBlockCollidingDown();
				Entity en = getEntityCollidingDown();
				if (c != null) {
					veloy = -veloy * bounceCoefficient;
					velo = 0;
					onCollideDown();
					world.getBlock(c.getX(), c.getY()).onCollideUpFace(world, c.getX(), c.getY(),
							this);
				} else if (en != null) {
					onCollideDown();
					onCollideEntityDown(en);
					velo = 0;
					float delta = Math.abs(this.veloy - en.veloy);
					en.veloy += delta * en.forceTransfer;
					this.veloy -= delta * en.forceTransfer;
					veloy = -veloy * bounceCoefficient;
				}
			} else if (velo < 0) {
				Coordinate c = getBlockCollidingUp();
				Entity en = getEntityCollidingUp();
				if (c != null) {
					veloy = -veloy * bounceCoefficient;
					velo = 0;
					onCollideUp();
					world.getBlock(c.getX(), c.getY()).onCollideDownFace(world, c.getX(), c.getY(),
							this);
				} else if (en != null) {
					onCollideUp();
					onCollideEntityUp(en);
					velo = 0;
					float delta = Math.abs(this.veloy - en.veloy);
					en.veloy += delta * en.forceTransfer;
					this.veloy -= delta * en.forceTransfer;
					veloy = -veloy * bounceCoefficient;
				}
			}
			for (int i = 0; i < Math.abs(velo); i++) {
				if (velo > 0) {
					y += World.tileparty;
					Coordinate c = getBlockCollidingDown();
					Entity en = getEntityCollidingDown();
					if (c != null) {
						veloy = -veloy * bounceCoefficient;
						onCollideDown();
						world.getBlock(c.getX(), c.getY()).onCollideUpFace(world, c.getX(),
								c.getY(), this);
						break;
					} else if (en != null) {
						onCollideDown();
						onCollideEntityDown(en);
						float delta = Math.abs(this.veloy - en.veloy);
						en.veloy += delta * en.forceTransfer;
						this.veloy -= delta * en.forceTransfer;
						veloy = -veloy * bounceCoefficient;
						break;
					}
				} else if (velo < 0) {
					y -= World.tileparty;
					Coordinate c = getBlockCollidingUp();
					Entity en = getEntityCollidingUp();
					if (c != null) {
						veloy = -veloy * bounceCoefficient;
						onCollideUp();
						world.getBlock(c.getX(), c.getY()).onCollideDownFace(world, c.getX(),
								c.getY(), this);
						break;
					} else if (en != null) {
						onCollideUp();
						onCollideEntityUp(en);
						float delta = Math.abs(this.veloy - en.veloy);
						en.veloy += delta * en.forceTransfer;
						this.veloy -= delta * en.forceTransfer;
						veloy = -veloy * bounceCoefficient;
						break;
					}
				}
			}
		}

		if (velox != 0) {

			int velo = (int) (velox / Main.TICKS * World.tilesizex);

			if (velo > 0) {
				Coordinate c = getBlockCollidingRight();
				Entity en = getEntityCollidingRight();
				if (c != null) {
					velox = -velox * bounceCoefficient;
					velo = 0;
					onCollideRight();
					world.getBlock(c.getX(), c.getY()).onCollideLeftFace(world, c.getX(), c.getY(),
							this);
				} else if (en != null) {
					onCollideRight();
					onCollideEntityRight(en);
					velo = 0;
					float delta = this.velox - en.velox;
					en.velox += delta * en.forceTransfer;
					this.velox -= delta * en.forceTransfer;
					velox = -velox * bounceCoefficient;
				}
			} else if (velo < 0) {
				Coordinate c = getBlockCollidingLeft();
				Entity en = getEntityCollidingLeft();
				if (c != null) {
					velox = -velox * bounceCoefficient;
					velo = 0;
					onCollideLeft();
					world.getBlock(c.getX(), c.getY()).onCollideRightFace(world, c.getX(),
							c.getY(), this);
				} else if (en != null) {
					onCollideLeft();
					onCollideEntityLeft(en);
					velo = 0;
					float delta = this.velox - en.velox;
					en.velox += delta * en.forceTransfer;
					this.velox -= delta * en.forceTransfer;
					velox = -velox * bounceCoefficient;
				}
			}
			for (int i = 0; i < Math.abs(velo); i++) {
				if (velo > 0) {
					x += World.tilepartx;
					Coordinate c = getBlockCollidingRight();
					Entity en = getEntityCollidingRight();
					if (c != null) {
						velox = -velox * bounceCoefficient;
						onCollideRight();
						world.getBlock(c.getX(), c.getY()).onCollideLeftFace(world, c.getX(),
								c.getY(), this);
						break;
					} else if (en != null) {
						onCollideRight();
						onCollideEntityRight(en);
						float delta = this.velox - en.velox;
						en.velox += delta * en.forceTransfer;
						this.velox -= delta * en.forceTransfer;
						velox = -velox * bounceCoefficient;
						break;
					}
				} else if (velo < 0) {
					x -= World.tilepartx;
					Coordinate c = getBlockCollidingLeft();
					Entity en = getEntityCollidingLeft();
					if (c != null) {
						velox = -velox * bounceCoefficient;
						onCollideLeft();
						world.getBlock(c.getX(), c.getY()).onCollideRightFace(world, c.getX(),
								c.getY(), this);
						break;
					} else if (en != null) {
						onCollideLeft();
						onCollideEntityLeft(en);
						float delta = this.velox - en.velox;
						en.velox += delta * en.forceTransfer;
						this.velox -= delta * en.forceTransfer;
						velox = -velox * bounceCoefficient;
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void writeToNBT(TagCompound tag){
		tag.setTag(new TagFloat("PosX", x));
		tag.setTag(new TagFloat("PosY", y));
		tag.setTag(new TagFloat("VeloX", velox));
		tag.setTag(new TagFloat("VeloY", veloy));
		tag.setTag(new TagLong("UUID", uuid));
		tag.setTag(new TagFloat("SizeX", sizex));
		tag.setTag(new TagFloat("SizeY", sizey));
	}
	
	@Override
	public void readFromNBT(TagCompound tag) throws TagNotFoundException, UnexpectedTagTypeException{
		uuid = tag.getLong("UUID");

		x = tag.getFloat("PosX");
		y = tag.getFloat("PosY");

		velox = tag.getFloat("VeloX");
		veloy = tag.getFloat("VeloY");

		sizex = tag.getFloat("SizeX");
		sizey = tag.getFloat("SizeY");
	}

	public boolean intersectingOther(Entity other) {
		return (MathHelper.intersects(x, y, sizex, sizey, other.x, other.y, other.sizex,
				other.sizey));
	}
	
	public void onCollideLeft() {

	}

	public void onCollideRight() {

	}

	public void onCollideUp() {

	}

	public void onCollideDown() {

	}

	public void onCollideEntityLeft(Entity e) {

	}

	public void onCollideEntityRight(Entity e) {

	}

	public void onCollideEntityUp(Entity e) {

	}

	public void onCollideEntityDown(Entity e) {

	}
	
	public Coordinate collidingAnyBlock() {
		Coordinate c = null;

		c = getBlockCollidingUp();
		if (c != null) return c;

		c = getBlockCollidingDown();
		if (c != null) return c;

		c = getBlockCollidingLeft();
		if (c != null) return c;

		c = getBlockCollidingRight();
		if (c != null) return c;

		return null;
	}

	public Entity getEntityCollidingUp() {
		for (Entity e : world.getQuadArea(this)) {
			if (e != this && e.hasEntityCollision) {
				if (((int) (x * World.tilesizex + sizex * World.tilesizex)) > ((int) (e.x * World.tilesizex))
						&& ((int) (x * World.tilesizex)) < ((int) (e.x * World.tilesizex + e.sizex
								* World.tilesizex))) {
					if (((int) (this.y * World.tilesizey)) == ((int) (e.y * World.tilesizey + e.sizey
							* World.tilesizey))) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public Entity getEntityCollidingDown() {
		for (Entity e : world.getQuadArea(this)) {
			if (e != this && e.hasEntityCollision) {
				if (((int) (x * World.tilesizex + sizex * World.tilesizex)) > ((int) (e.x * World.tilesizex))
						&& ((int) (x * World.tilesizex)) < ((int) (e.x * World.tilesizex + e.sizex
								* World.tilesizex))) {
					if (((int) (e.y * World.tilesizey)) == ((int) (this.y * World.tilesizey + this.sizey
							* World.tilesizey))) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public Entity getEntityCollidingLeft() {
		for (Entity e : world.getQuadArea(this)) {
			if (e != this && e.hasEntityCollision) {
				if (((int) (y * World.tilesizey + sizey * World.tilesizey)) > ((int) (e.y * World.tilesizey))
						&& ((int) (y * World.tilesizey)) < ((int) (e.y * World.tilesizey + e.sizey
								* World.tilesizey))) {
					if (((int) (this.x * World.tilesizex)) == ((int) (e.x * World.tilesizex + e.sizex
							* World.tilesizex))) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public Entity getEntityCollidingRight() {
		for (Entity e : world.getQuadArea(this)) {
			if (e != this && e.hasEntityCollision) {
				if (((int) (y * World.tilesizey + sizey * World.tilesizey)) > ((int) (e.y * World.tilesizey))
						&& ((int) (y * World.tilesizey)) < ((int) (e.y * World.tilesizey + e.sizey
								* World.tilesizey))) {
					if (((int) (e.x * World.tilesizex)) == ((int) (this.x * World.tilesizex + this.sizex
							* World.tilesizex))) {
						return e;
					}
				}
			}
		}

		return null;
	}

	public Coordinate getBlockCollidingUp() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if (posy % World.tilesizey != 0) return null;
		if (y <= 0) return Coordinate.global.setPosition((int) x, (int) y);

		for (int i = 0; i < boundx; i++) {
			if ((world.getBlock((posx + i) / World.tilesizex, (posy / World.tilesizey) - 1)
					.isSolid(world, (posx + i) / World.tilesizex, (posy / World.tilesizey) - 1) & BlockFaces.DOWN) == BlockFaces.DOWN) {
				return Coordinate.global.setPosition((posx + i) / World.tilesizex,
						(posy / World.tilesizey) - 1);
			}
		}

		return null;
	}

	public Coordinate getBlockCollidingDown() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posy + boundy) % World.tilesizey != 0) return null;
		if ((y + sizey) >= world.sizey) return Coordinate.global.setPosition((int) x, (int) y);

		for (int i = 0; i < boundx; i++) {
			if ((world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) & BlockFaces.UP) == BlockFaces.UP) {
				return Coordinate.global.setPosition((posx + i) / World.tilesizex,
						((posy + boundy) / World.tilesizey));
			}
		}

		return null;
	}

	public float getLowestDrag() {
		if (getBlockCollidingDown() == null) return BlockEmpty.DRAG;

		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posy + boundy) % World.tilesizey != 0) return 1;
		if ((y + sizey) >= world.sizey) return 1;

		float lowest = 1;

		for (int i = 0; i < boundx; i++) {
			if ((world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) & BlockFaces.UP) == BlockFaces.UP) {
				if (world.getBlock((posx + i) / World.tilesizex,
						((posy + boundy) / World.tilesizey)).getDragCoefficient(world,
						(posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey)) < lowest) {
					lowest = world.getBlock((posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)).getDragCoefficient(world,
							(posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey));
				}
			}
		}

		return lowest;
	}

	public float getHighestDrag() {
		if (getBlockCollidingDown() == null) return BlockEmpty.DRAG;

		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posy + boundy) % World.tilesizey != 0) return 1;
		if ((y + sizey) >= world.sizey) return 1;

		float highest = Float.MIN_NORMAL;

		for (int i = 0; i < boundx; i++) {
			if ((world.getBlock((posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey))
					.isSolid(world, (posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)) & BlockFaces.UP) == BlockFaces.UP) {
				if (world.getBlock((posx + i) / World.tilesizex,
						((posy + boundy) / World.tilesizey)).getDragCoefficient(world,
						(posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey)) > highest) {
					highest = world.getBlock((posx + i) / World.tilesizex,
							((posy + boundy) / World.tilesizey)).getDragCoefficient(world,
							(posx + i) / World.tilesizex, ((posy + boundy) / World.tilesizey));
				}
			}
		}

		return highest;
	}

	public Coordinate getBlockCollidingLeft() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if (posx % World.tilesizex != 0) return null;
		if (posx <= 0) return Coordinate.global.setPosition((int) x, (int) y);

		for (int i = 0; i < boundy; i++) {
			if ((world.getBlock(((posx) / World.tilesizex) - 1, ((posy + i) / World.tilesizey))
					.isSolid(world, ((posx) / World.tilesizex) - 1, ((posy + i) / World.tilesizey)) & BlockFaces.RIGHT) == BlockFaces.RIGHT) {
				return Coordinate.global.setPosition(((posx) / World.tilesizex) - 1,
						((posy + i) / World.tilesizey));
			}
		}

		return null;
	}

	public Coordinate getBlockCollidingRight() {
		int posx = (int) (x * World.tilesizex);
		int posy = (int) (y * World.tilesizey);
		int boundx = (int) (sizex * World.tilesizex);
		int boundy = (int) (sizey * World.tilesizey);

		if ((posx + boundx) % World.tilesizex != 0) return null;
		if ((x + sizex) >= (world.sizex)) return Coordinate.global.setPosition((int) x, (int) y);

		for (int i = 0; i < boundy; i++) {
			if ((world
					.getBlock(((posx + boundx) / World.tilesizex), ((posy + i) / World.tilesizey))
					.isSolid(world, ((posx + boundx) / World.tilesizex),
							((posy + i) / World.tilesizey)) & BlockFaces.LEFT) == BlockFaces.LEFT) {
				return Coordinate.global.setPosition(((posx + boundx) / World.tilesizex),
						((posy + i) / World.tilesizey));
			}
		}

		return null;
	}

	public Entity entityColliding() {
		if (!hasEntityCollision) return null;
		for (Entity e : world.getQuadArea(this)) {
			if (intersectingOther(e)) {
				if (!e.hasEntityCollision) continue;
				if (e == this) continue;
				return e;
			}
		}

		return null;
	}
	
	public void accelerate(float x, float y, boolean limitSpeed) {
		if (x > 0) {
			velox += (x + (world.drag * Gdx.graphics.getDeltaTime()))
					* Math.max(World.tilepartx, Math.abs(getHighestDrag()));
			if (limitSpeed) if (velox > maxspeed) velox = maxspeed;
		} else if (x < 0) {
			velox += (x - (world.drag * Gdx.graphics.getDeltaTime()))
					* Math.max(World.tilepartx, Math.abs(getHighestDrag()));
			if (limitSpeed) if (velox < -maxspeed) velox = -maxspeed;
		}
		if (y > 0) {
			veloy += y + (world.drag * Gdx.graphics.getDeltaTime());
			// if (dragcalc) if (veloy > maxspeed) veloy = maxspeed;
		} else if (y < 0) {
			veloy += y - (world.drag * Gdx.graphics.getDeltaTime());
			// if (dragcalc) if (veloy < -maxspeed) veloy = -maxspeed;
		}

	}

	public void accelerate(float x, float y) {
		accelerate(x, y, false);
	}

	public void moveUp() {
		if (getBlockCollidingUp() == null && veloy > -maxspeed) {
			accelerate(0, -accspeed * Gdx.graphics.getDeltaTime(), true);
		}
	}

	public void moveDown() {
		if (getBlockCollidingDown() == null && veloy < maxspeed) {
			accelerate(0, accspeed * Gdx.graphics.getDeltaTime(), true);
		}
	}

	public void moveLeft() {
		if (getBlockCollidingLeft() == null && velox > -maxspeed) {
			accelerate(-accspeed * Gdx.graphics.getDeltaTime(), 0, true);
		}
	}

	public void moveRight() {
		if (getBlockCollidingRight() == null && velox < maxspeed) {
			accelerate(accspeed * Gdx.graphics.getDeltaTime(), 0, true);
		}
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getWidth() {
		return sizex;
	}

	@Override
	public float getHeight() {
		return sizey;
	}

}
