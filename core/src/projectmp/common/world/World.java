package projectmp.common.world;

import java.util.ArrayList;

import projectmp.common.Main;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.entity.Entity;
import projectmp.common.util.Particle;
import projectmp.common.util.QuadTree;
import projectmp.common.util.SimplexNoise;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
	Block[][] blocks;
	int[][] meta;
	
	public transient boolean isServer = false;
	
	public Array<Particle> particles;
	public Array<Entity> entities;
	
	public QuadTree quadtree;
	private ArrayList<Entity> quadlist = new ArrayList<Entity>();
	
	public SimplexNoise noiseGen;
	public long seed = System.currentTimeMillis();
	
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
		blocks = new Block[sizex][sizey];
		meta = new int[sizex][sizey];

		for (int j = 0; j < sizex; j++) {
			for (int k = 0; k < sizey; k++) {
				blocks[j][k] = Blocks.instance().getBlock(Blocks.defaultBlock);
				meta[j][k] = 0;
			}
		}

		entities = new Array<Entity>(32);
		particles = new Array<Particle>();
		quadtree = new QuadTree(1, new Rectangle(0f, 0f, sizex, sizey));
		
		noiseGen = new SimplexNoise(seed);
	}
	
	public void tickUpdate(){
		if(isServer){
			for(int i = 0; i < entities.size; i++){
				Entity e = entities.get(i);
				e.tickUpdate();
			}
		}else{
			
		}
		
		quadtree.clear();
		for(int i = 0; i < entities.size; i++){
			quadtree.insert(entities.get(i));
		}
	}
	
	public ArrayList<Entity> getQuadArea(Entity e){
		quadlist.clear();
		quadtree.retrieve(quadlist, e);
		return quadlist;
	}
	
	public long getUniqueUUID(){
		long id = MathUtils.random(Long.MIN_VALUE + 1, Long.MAX_VALUE - 1);
		for(int i = 0; i < entities.size; i++){
			if(entities.get(i).uuid == id){
				return getUniqueUUID();
			}
		}
		return id;
	}
	
	public void generate(){
		float hillCoeff = 8f;
		
		for(int x = 0; x < sizex; x++){
			for(int y = Math.round(hillCoeff * 2); y < sizey; y++){
				setBlock(Blocks.instance().getBlock("stone"), x, y);
			}
		}
		
		for(int i = 0; i < sizex; i++){
			double noise = noiseGen.eval(i * 0.15f, -1 * 0.15f);
			int actualHeight = (int) Math.round(noise * hillCoeff);
			
			for(int y = actualHeight + (sizey / 4); y >= actualHeight + 10; y--){
				setBlock(Blocks.instance().getBlock("dirt"), i, y);
			}
			
			setBlock(Blocks.instance().getBlock("grass"), i, actualHeight + 10);
		}
		
		float caveStart = 0.4f;
		for(int x = 0; x < sizex; x++){
			for(int y = Math.round(hillCoeff * 2); y < sizey; y++){
				float noise = (float) noiseGen.eval(x * 0.1f, y * 0.1f);
				noise = (noise + 1) / 2f;
				if(noise >= caveStart){
					setBlock(Blocks.instance().getBlock("empty"), x, y);
				}
			}
		}
		for(int x = 0; x < sizex; x++){
			for(int y = 0; y < Math.round(hillCoeff * 2); y++){
				float noise = (float) noiseGen.eval(x * 0.2f, y * 0.2f);
				noise = (noise + 1) / 2f;
				if(noise >= caveStart * 1.2f){
					setBlock(Blocks.instance().getBlock("empty"), x, y);
				}
			}
		}
		
	}
	
	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return Blocks.defaultBlock();
		if (blocks[x][y] == null) return Blocks.defaultBlock();
		return blocks[x][y];
	}

	public int getMeta(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return 0;
		return meta[x][y];
	}

	public void setBlock(Block b, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		blocks[x][y] = b;
	}

	public void setMeta(int m, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		meta[x][y] = m;
	}
	
}
