package projectmp.client;

import projectmp.common.Main;
import projectmp.common.world.World;


public class WorldGeneratingScreen extends MiscLoadingScreen{

	private World world;
	
	public WorldGeneratingScreen(Main m) {
		super(m);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

		main.batch.begin();
		
		
		
		main.batch.end();
	}
	
	public void setWorld(World world){
		this.world = world;
	}

}
