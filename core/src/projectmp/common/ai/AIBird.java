package projectmp.common.ai;

import projectmp.common.Main;
import projectmp.common.block.Block.BlockFaces;
import projectmp.common.entity.Entity;

import com.badlogic.gdx.math.MathUtils;


public class AIBird extends BaseAI {

	float targetX, targetY;
	boolean flying = true;
	
	public AIBird(Entity e) {
		super(e);
		targetX = e.x;
		targetY = e.y;
		
		repickTarget();
	}

	@Override
	public void tickUpdate() {
		if(targetX < entity.x){
			entity.moveLeft(true);
		}else if(targetX > entity.x){
			entity.moveRight(true);
		}
		
		if(targetY < entity.y){
			entity.moveUp(true);
		}else if(targetY > entity.y){
			entity.moveDown(true);
		}
		
//		targetX = entity.world.main.clientLogic.getCursorBlockX();
//		targetY = entity.world.main.clientLogic.getCursorBlockY();
		
		if(MathUtils.randomBoolean(0.01f)){
			flying = !flying;
			
			repickTarget();
		}
	}
	
	private void repickTarget(){
		targetX = entity.x + MathUtils.random(10, 15) * MathUtils.randomSign();
		targetY = entity.y - MathUtils.random(5);
		
		if(!flying){
			for(int y = (int) targetY; y < entity.world.sizey; y++){
				if((entity.world.getBlock((int) targetX, y).isSolid(entity.world, (int) targetX, y) & BlockFaces.UP) == BlockFaces.UP){
					targetY = y + entity.sizey;
				}
			}
		}
	}

}
