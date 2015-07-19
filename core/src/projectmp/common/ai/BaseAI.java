package projectmp.common.ai;

import projectmp.common.entity.Entity;


public abstract class BaseAI {
	
	Entity entity;
	
	public BaseAI(Entity e){
		this.entity = e;
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public void setEntity(Entity e){
		entity = e;
	}
	
	public abstract void tickUpdate();
	
}
