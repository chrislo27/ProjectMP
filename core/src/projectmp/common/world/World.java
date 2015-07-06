package projectmp.common.world;

import java.util.ArrayList;

import projectmp.client.background.Background;
import projectmp.client.lighting.LightingEngine;
import projectmp.common.Main;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.chunk.Chunk;
import projectmp.common.entity.Entity;
import projectmp.common.entity.ILoadsChunk;
import projectmp.common.packet.PacketNewEntity;
import projectmp.common.packet.PacketRemoveEntity;
import projectmp.common.packet.PacketSendTileEntity;
import projectmp.common.packet.repository.PacketRepository;
import projectmp.common.tileentity.ITileEntityProvider;
import projectmp.common.tileentity.TileEntity;
import projectmp.common.util.Particle;
import projectmp.common.util.ParticlePool;
import projectmp.common.util.QuadTree;
import projectmp.common.util.SimplexNoise;
import projectmp.common.util.Sizeable;
import projectmp.common.util.Utils;
import projectmp.common.weather.Weather;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class World {

	public static final int tilesizex = 32;
	public static final int tilesizey = 32;
	public static final float tilepartx = 1.0f / tilesizex;
	public static final float tileparty = 1.0f / tilesizey;

	public Main main;
	public SpriteBatch batch;

	public float gravity = 20f;
	public float drag = 20f;

	public int sizex;
	public int sizey;
	Chunk[][] chunks;
	private int[][] loadedChunks;

	public transient LightingEngine lightingEngine;

	public transient boolean isServer = false;

	public Array<Particle> particles;
	protected Array<Entity> entities;

	public QuadTree quadtree;
	private ArrayList<Entity> quadlist = new ArrayList<Entity>();

	public SimplexNoise noiseGen;
	public long seed = System.currentTimeMillis();

	public Time time = new Time();

	public Background background = new Background(this);

	private Weather weather = null;

	/**
	 * 
	 * @param main Main instance
	 * @param x width
	 * @param y height
	 * @param server true = server-side
	 * @param seed seed
	 */
	public World(Main main, int x, int y, boolean server, long seed) {
		this.main = main;
		batch = main.batch;
		sizex = x;
		sizey = y;
		isServer = server;
		this.seed = seed;
		prepare();
	}

	public void prepare() {
		// set up chunks
		chunks = new Chunk[getWidthInChunks()][getHeightInChunks()];
		loadedChunks = new int[getWidthInChunks()][getHeightInChunks()];

		// fill in chunks
		for (int x = 0; x < getWidthInChunks(); x++) {
			for (int y = 0; y < getHeightInChunks(); y++) {
				chunks[x][y] = new Chunk(x, y);
			}
		}

		lightingEngine = new LightingEngine(this);

		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				setBlock(Blocks.defaultBlock(), x, y);
				setMeta(0, x, y);
			}
		}

		entities = new Array<Entity>(32);
		particles = new Array<Particle>();
		quadtree = new QuadTree(sizex, sizey);

		noiseGen = new SimplexNoise(seed);
	}

	public void tickUpdate() {
		time.tickUpdate();

		loadChunksNearLoaders();
		
		// tick update loaded chunks
		for (int x = 0; x < getWidthInChunks(); x++) {
			for (int y = 0; y < getHeightInChunks(); y++) {
				if (isChunkLoaded(x, y)) {
					getChunk(x, y).tickUpdate(this);

					loadedChunks[x][y]--;
				}
			}
		}

		if (isServer) {

			for (int i = 0; i < entities.size; i++) {
				Entity e = entities.get(i);
				e.tickUpdate();
			}

			if (particles.size > 0) {
				Particle item;
				for (int i = particles.size; --i >= 0;) {
					item = particles.get(i);
					// remove immediately since server doesn't need particles
					particles.removeIndex(i);
					ParticlePool.instance().getPool().free(item);
				}
			}
		} else {
			if (particles.size > 0) {
				Particle item;
				for (int i = particles.size; --i >= 0;) {
					item = particles.get(i);
					if (item.lifetime <= 0) {
						particles.removeIndex(i);
						ParticlePool.instance().getPool().free(item);
					}
				}
			}
		}

		quadtree.clear();
		for (int i = 0; i < entities.size; i++) {
			quadtree.insert(entities.get(i));
		}

		if (weather != null) {
			weather.tickDownTimeRemaining();

			if (weather.getTimeRemaining() > 0) {
				weather.tickUpdate();
			} else {
				weather = null;
			}
		}
	}

	public ArrayList<Entity> getQuadArea(Sizeable e) {
		quadlist.clear();
		quadtree.retrieve(quadlist, e);
		return quadlist;
	}

	public long getNewUniqueUUID() {
		long id = MathUtils.random(Long.MIN_VALUE + 1, Long.MAX_VALUE - 1);
		for (int i = 0; i < entities.size; i++) {
			if (entities.get(i).uuid == id) {
				return getNewUniqueUUID();
			}
		}
		return id;
	}

	public Entity getEntityByUUID(long uuid) {
		for (int i = 0; i < entities.size; i++) {
			if (entities.get(i).uuid == uuid) {
				return entities.get(i);
			}
		}

		return null;
	}

	public Entity getEntityByIndex(int i) {
		return entities.get(i);
	}

	public void createNewEntity(Entity e) {
		if (isServer) {
			PacketNewEntity packet = PacketRepository.instance().newEntity;
			packet.e = e;
			main.server.sendToAllTCP(packet);
		} else {
			entities.add(e);
		}
	}

	public void removeEntity(long uuid) {
		if (isServer) {
			PacketRemoveEntity packet = PacketRepository.instance().removeEntity;
			packet.uuid = uuid;
			main.server.sendToAllTCP(packet);
		} else {
			Entity e = getEntityByUUID(uuid);
			if (e == null) return;
			entities.removeValue(e, true);
		}
	}

	public long getUUIDFromIndex(int index) {
		Entity e = getEntityByIndex(index);

		if (e != null) {
			return e.uuid;
		} else {
			return Long.MAX_VALUE;
		}
	}

	public int getNumberOfEntities() {
		return entities.size;
	}

	public void clearAllEntities() {
		for (int i = 0; i < getNumberOfEntities(); i++) {
			long uuid = getUUIDFromIndex(i);
			removeEntity(uuid);
		}
	}

	/**
	 * loads the chunks near entities that implement ILoadsChunk
	 */
	public void loadChunksNearLoaders() {
		for (int i = 0; i < entities.size; i++) {
			Entity e = entities.get(i);

			if (e instanceof ILoadsChunk) {
				int cx = getChunkX((int) e.x);
				int cy = getChunkY((int) e.y);

				for (int x = cx - 3; x < cx + 3; x++) {
					for (int y = cy - 3; y < cy + 3; y++) {
						loadChunk(x, y, Main.TICKS);
					}
				}
			}
		}
	}

	// FIXME make generator better
	public void generate() {
		float hillCoeff = 8f;
		float terrainLimit = hillCoeff + (sizey / 8);
		int dirtLimit = Math.round(hillCoeff + (sizey / 8));
		int caveStart = dirtLimit - (sizey / 32);

		for (int i = 0; i < sizex; i++) {
			float noise = (float) noiseGen.eval(i * 0.09f, -1 * 0.09f);
			int actualHeight = Math.round(noise * hillCoeff);

			int topOfDirt = actualHeight + (sizey / 16);
			int endOfDirt = actualHeight + (sizey / 8);
			for (int y = endOfDirt; y >= topOfDirt; y--) {
				setBlock(Blocks.instance().getBlock("dirt"), i, y);
			}
			setBlock(Blocks.instance().getBlock("grass"), i, actualHeight + (sizey / 16));
			setBlock(Blocks.instance().getBlock(i == sizex / 2 ? "chess_set" : "tall_grass"), i,
					actualHeight + (sizey / 16) - 1);

			for (int y = endOfDirt; y < sizey; y++) {
				setBlock(Blocks.instance().getBlock("stone"), i, y);
			}

			// long cave under surface to cover up noise's abrupt end
			for (int y = caveStart - Math.round(4 * noise) - 6; y < caveStart
					+ Math.round(3 * noise) + 3; y++) {
				setBlock(Blocks.instance().getBlock("empty"), i, y);
			}
		}

		/* NOTES
		 * Terrain upper limit is essentially 1 * hillCoeff + (sizey / 8)
		 * dirt ends at around hillCoeff + (sizey / 8)
		 * noise ends at dirtLimit - (sizey / 32)
		 * caves start at dirtLimit - (sizey / 32)
		 * 
		 */

		float caveStartThreshold = 0.56f;
		float caveEndThreshold = 0.825f;
		for (int x = 0; x < sizex; x++) {
			for (int y = caveStart; y < sizey; y++) {
				float noise = (float) noiseGen.eval(x * 0.1f, y * 0.1f);
				noise = (noise + 1) / 2f;
				if (noise >= caveStartThreshold && noise <= caveEndThreshold) {
					setBlock(Blocks.instance().getBlock("empty"), x, y);
				}
			}
		}

		Pixmap pix = new Pixmap(sizex, sizey, Format.RGBA8888);
		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				pix.setColor(0, 0, 0, 1);
				if (getBlock(x, y) == Blocks.instance().getBlock("dirt")) {
					pix.setColor(140 / 255f, 96 / 255f, 45 / 255f, 1);
				} else if (getBlock(x, y) == Blocks.instance().getBlock("grass")) {
					pix.setColor(40 / 255f, 176 / 255f, 50 / 255f, 1);
				} else if (getBlock(x, y) == Blocks.instance().getBlock("stone")) {
					pix.setColor(0.5f, 0.5f, 0.5f, 1);
				}
				pix.drawPixel(x, y);
			}
		}
		PixmapIO.writePNG(new FileHandle("noisemaps/worldmap.png"), pix);
		pix.dispose();

	}

	public void setWeather(Weather w) {
		weather = w;
	}

	public Weather getWeather() {
		return weather;
	}

	public int getWidthInChunks() {
		return Math.max(1, sizex / 16);
	}

	public int getHeightInChunks() {
		return Math.max(1, sizey / 16);
	}

	public Chunk getChunkBlockIsIn(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return null;

		return chunks[getChunkX(x)][getChunkY(y)];
	}

	public Chunk getChunk(int chunkx, int chunky) {
		if (chunkx < 0 || chunky < 0 || chunkx >= getWidthInChunks()
				|| chunky >= getHeightInChunks()) return null;

		return chunks[chunkx][chunky];
	}

	/**
	 * returns the chunk the block's X coordinate is at
	 * @param blockx
	 * @return
	 */
	public int getChunkX(int blockx) {
		if (blockx < 0 || blockx >= sizex) return -1;

		return blockx / Chunk.CHUNK_SIZE;
	}

	/**
	 * returns the chunk the block's Y coordinate is at
	 * @param blocky
	 * @return
	 */
	public int getChunkY(int blocky) {
		if (blocky < 0 || blocky >= sizey) return -1;

		return blocky / Chunk.CHUNK_SIZE;
	}

	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return Blocks.defaultBlock();

		return getChunkBlockIsIn(x, y).getChunkBlock(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
	}

	public int getMeta(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return 0;

		return getChunkBlockIsIn(x, y).getChunkMeta(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
	}

	public TileEntity getTileEntity(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return null;

		return getChunkBlockIsIn(x, y).getChunkTileEntity(x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
	}

	public void setBlock(Block b, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;

		getChunkBlockIsIn(x, y).setChunkBlock(b, x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
		lightingEngine.scheduleLightingUpdate();

		if (b instanceof ITileEntityProvider) {
			setTileEntity(((ITileEntityProvider) b).createNewTileEntity(x, y), x, y);
		}
	}

	public void setMeta(int m, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;

		getChunkBlockIsIn(x, y).setChunkMeta(m, x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
	}

	public void setTileEntity(TileEntity te, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;

		getChunkBlockIsIn(x, y).setChunkTileEntity(te, x % Chunk.CHUNK_SIZE, y % Chunk.CHUNK_SIZE);
	}

	public boolean isChunkLoaded(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return false;

		return loadedChunks[x][y] > 0;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param length
	 * @return false if out of bounds, true otherwise
	 */
	public boolean loadChunk(int x, int y, int length) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return false;

		loadedChunks[x][y] = length;
		return true;
	}

	public void sendTileEntityUpdate(int x, int y) {
		PacketSendTileEntity tepacket = PacketRepository.instance().sendTileEntity;

		tepacket.te = getTileEntity(x, y);
		tepacket.x = x;
		tepacket.y = y;

		main.server.sendToAllTCP(tepacket);
	}

}
