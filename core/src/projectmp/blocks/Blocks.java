package projectmp.blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import projectmp.Main;
import projectmp.LevelEditor.EditorGroup;
import projectmp.blocks.Block.BlockFaces;
import projectmp.entity.Entity;
import projectmp.entity.EntityBall;
import projectmp.entity.EntityWhale;
import projectmp.entity.EntityZaborinox;
import projectmp.world.World;

import com.badlogic.gdx.graphics.Color;

public class Blocks {

	private static Blocks instance;

	private Blocks() {
	}

	public static Blocks instance() {
		if (instance == null) {
			instance = new Blocks();
			instance.loadResources();
		}
		return instance;
	}

	private HashMap<String, Block> blocks;
	private HashMap<Block, String> reverse;
	
	private HashMap<String, String> oldLookup;
	
	public static final Color RED = new Color(1, 0, 0, 1);
	public static final Color GREEN = new Color(16 / 255f, 164 / 255f, 43 / 255f, 1);
	public static final Color BLUE = new Color(0, 145 / 255f, 1, 1);
	public static final Color PURPLE = new Color(178 / 255f, 0, 1, 1);
	public static final Color ORANGE = new Color(1, 106 / 255f, 0, 1);

	private void loadResources() {
		blocks = new HashMap<String, Block>();
		reverse = new HashMap<Block, String>();
		
		oldLookup = new HashMap<String, String>();

		put("space", new BlockOuterSpace("images/blocks/old/space/space").hasVariants(8));
		put("wall", new Block("images/blocks/old/dungeonwall/wall").useConTextures().solidify(BlockFaces.ALL));
		put("empty", new BlockEmpty());
		put("spike", new BlockSpike("images/blocks/spike").setEditorGroup(EditorGroup.HAZARD));
		put("sign", new BlockReadable("images/blocks/sign/sign").hasVariants(4));
		put("ice", new BlockIce("images/blocks/ice/ice").solidify(BlockFaces.ALL));
		
		put("switchred", new BlockSwitch(RED, "red").solidify(BlockFaces.ALL));
		put("switchgreen", new BlockSwitch(GREEN, "green").solidify(BlockFaces.ALL));
		put("switchblue", new BlockSwitch(BLUE, "blue").solidify(BlockFaces.ALL));
		put("switchpurple", new BlockSwitch(PURPLE, "purple").solidify(BlockFaces.ALL));
		put("switchorange", new BlockSwitch(ORANGE, "orange").solidify(BlockFaces.ALL));
 		
		put("togglered", new BlockToggle(RED, "red").solidify(BlockFaces.ALL));
		put("togglegreen", new BlockToggle(GREEN, "green").solidify(BlockFaces.ALL));
		put("toggleblue", new BlockToggle(BLUE, "blue").solidify(BlockFaces.ALL));
		put("togglepurple", new BlockToggle(PURPLE, "purple").solidify(BlockFaces.ALL));
		put("toggleorange", new BlockToggle(ORANGE, "orange").solidify(BlockFaces.ALL));
		
		put("timerred", new BlockTimer(RED, "red"));
		put("timergreen", new BlockTimer(GREEN, "green"));
		put("timerblue", new BlockTimer(BLUE, "blue"));
		put("timerpurple", new BlockTimer(PURPLE, "purple"));
		put("timerorange", new BlockTimer(ORANGE, "orange"));
		
		put("exitportal", new BlockExitPortal("images/blocks/exit/exit"));
		put("platform", new BlockPlatform("images/blocks/platform/platform"));
		put("cameramagnet", new BlockCameraMagnet("images/blocks/magnet"));
		put("electrode", new BlockElectrode("images/blocks/electrode/electrode").setEditorGroup(EditorGroup.HAZARD));
		put("fire", new BlockFire(null).setAnimation("fire").setEditorGroup(EditorGroup.HAZARD));
		put("airvent", new BlockAirVent("images/blocks/airvent/airvent").solidify(BlockFaces.ALL));
		put("3dblock", new Block("images/blocks/3d/3dblock").solidify(BlockFaces.ALL));
		put("teleporter", new BlockTeleporter("images/blocks/teleporter/teleporter"));
		put("buleahteststone", new Block("images/blocks/test/Copy of SMAhXdn").solidify(BlockFaces.ALL));
		
		put("objectiveblock", new BlockObjectiveNew("images/blocks/objective/new").setEditorGroup(EditorGroup.COLLECT));
		put("objectivecompleteblock", new BlockObjectiveFinish("images/blocks/objective/finish").setEditorGroup(EditorGroup.COLLECT));
		
		put("gearCollectible", new BlockGearCollectible("images/blocks/collectible/gear"));
		
		put("checkpointclaimed", new Block("images/blocks/checkpoint/checkpointclaimed"){
			@Override
			public boolean isRenderedFront(){
				return true;
			}
		}.setEditorGroup(null));
		put("checkpointunclaimed", new BlockCheckpoint("images/blocks/checkpoint/checkpointnew"));
		
		put("jumppad", new BlockJumpPad(null).setAnimation("jumppad").solidify(BlockFaces.ALL));
		put("accelerationpad", new BlockAccPad(null).setAnimation("accelerationpad").solidify(BlockFaces.ALL));
		
		// spawners
		put("spawnerplayer", new BlockPlayerSpawner("images/entity/player/player"));
		put("spawnerzaborinox", new BlockSpawner("images/entity/zaborinox"){
			public Entity getEntity(World world, int x, int y){
				return new EntityZaborinox(world, x, y);
			}
		});
		put("spawnerwhale", new BlockSpawner("images/entity/whale"){
			public Entity getEntity(World world, int x, int y){
				return new EntityWhale(world, x, y);
			}
		});
		put("spawnerball", new BlockSpawner("images/entity/ball"){
			public Entity getEntity(World world, int x, int y){
				return new EntityBall(world, x, y);
			}
		});
		
		

	}

	private void put(String key, Block value) {
		blocks.put(key, value);
		reverse.put(value, key);
	}

	public Block getBlock(String key) {
		if (key == null) return defaultBlock();
		if(oldLookup.get(key) != null) return blocks.get(oldLookup.get(key));
		return blocks.get(key);
	}

	public String getKey(Block block) {
		if (block == null) return defaultBlock;
		return reverse.get(block);
	}

	public Iterator getAllBlocks() {
		return blocks.entrySet().iterator();
	}

	public void dispose() {
		Iterator it = getAllBlocks();
		while (it.hasNext()) {
			Entry pairs = (Entry) it.next();
			Block block = (Block) pairs.getValue();
			block.dispose();
		}
	}

	/**
	 * must be called manually
	 * 
	 * @param main
	 */
	public void addBlockTextures(Main main) {
		Iterator it = getAllBlocks();
		while (it.hasNext()) {
			Entry pairs = (Entry) it.next();
			Block block = (Block) pairs.getValue();
			block.addTextures(main);
		}
	}

	public static Block defaultBlock() {
		return instance().getBlock(defaultBlock);
	}

	public static final String defaultBlock = "empty";
}
