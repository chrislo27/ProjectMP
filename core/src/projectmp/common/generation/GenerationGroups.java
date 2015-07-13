package projectmp.common.generation;

import projectmp.common.block.Blocks;
import projectmp.common.generation.terrain.TerrainLandscape;
import projectmp.common.world.World;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Array;

/**
 * Singleton of all the registered generation groups.
 * 
 *
 */
public class GenerationGroups {

	private static GenerationGroups instance;

	private GenerationGroups() {
	}

	public static GenerationGroups instance() {
		if (instance == null) {
			instance = new GenerationGroups();
			instance.loadResources();
		}
		return instance;
	}

	private Array<GenerationGroup> groups = new Array<>();
	
	private void loadResources() {
		// add stock
		addGroup(new TerrainLandscape());
	}
	
	/**
	 * Adds and sorts a group
	 * @param g
	 */
	public synchronized void addGroup(GenerationGroup g){
		groups.add(g);
		groups.sort();
	}
	
	public synchronized Array<GenerationGroup> getAllGroups(){
		return groups;
	}
	
	public static void generateWorld(World world){
		for(int i = 0; i < instance().groups.size; i++){
			instance().groups.get(i).generate(world, world.noiseGen);
		}
		
		Pixmap pix = new Pixmap(world.sizex, world.sizey, Format.RGBA8888);
		for(int x = 0; x < world.sizex; x++){
			for(int y = 0; y < world.sizey; y++){
				pix.setColor(1, 1, 1, 1);
				pix.drawPixel(x, y);
				
				float noise = (float) world.noiseGen.eval(x * 0.05, y * 0.05);
				noise = (noise + 1) / 2f;
				
				if(world.getBlock(x, y) != Blocks.instance().getBlock("empty")){
					pix.setColor(0, 0, 0, 1);
				}
				pix.drawPixel(x, y);
			}
		}
		PixmapIO.writePNG(new FileHandle("noisemaps/worldmap.png"), pix);
		pix.dispose();
	}
	
}
