package projectmp.common.world;

import java.util.ArrayList;

import projectmp.common.Main;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.entity.Entity;
import projectmp.common.util.Particle;
import projectmp.common.util.QuadTree;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class World {

	public static final int tilesizex = 64;
	public static final int tilesizey = 64;
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
	
	public boolean isServer = false;
	
	public Array<Particle> particles;
	public Array<Entity> entities;
	
	public QuadTree quadtree;
	private ArrayList<Entity> quadlist = new ArrayList<Entity>();
	
	public World(Main main, int x, int y, boolean server) {
		this.main = main;
		batch = main.batch;
		sizex = x;
		sizey = y;
		isServer = server;
		prepare();
	}

	public void prepare() {
		blocks = new Block[sizex][sizey];
		meta = new int[sizex][sizey];

		for (int j = 0; j < sizex; j++) {
			for (int k = 0; k < sizey; k++) {
				blocks[j][k] = Blocks.instance().getBlock(Blocks.defaultBlock);
				meta[j][k] = 0;
				
				if(k > 5) blocks[j][k] = Blocks.instance().getBlock("stone");
			}
		}

		entities = new Array<Entity>(32);
		particles = new Array<Particle>();
		quadtree = new QuadTree(1, new Rectangle(0f, 0f, sizex, sizey));
	}
	
	public void tickUpdate(){
		if(isServer){
			for(Entity e : entities){
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
