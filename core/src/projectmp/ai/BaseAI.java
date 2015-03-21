package projectmp.ai;

import projectmp.entity.Entity;


public abstract class BaseAI {

	Entity entity;
	public BaseAI(Entity e){
		this.entity = e;
	}
	
	public abstract void tickUpdate();
	
	public abstract void renderUpdate();
	
}
