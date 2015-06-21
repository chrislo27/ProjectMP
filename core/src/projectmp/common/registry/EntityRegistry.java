package projectmp.common.registry;

import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityLiving;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.registry.classmap.ClassMap;


public class EntityRegistry{

	private static EntityRegistry instance;

	private EntityRegistry() {
	}

	public static EntityRegistry instance() {
		if (instance == null) {
			instance = new EntityRegistry();
			instance.loadResources();
		}
		return instance;
	}
	
	private ClassMap<Entity> entityClassMap = new ClassMap<>();
	
	private void loadResources() {
		registerEntity(EntityLiving.class, "entityLiving");
		registerEntity(EntityPlayer.class, "player");
	}
	
	public void registerEntity(Class<? extends Entity> clazz, String key){
		entityClassMap.register(clazz, key);
	}
	
	public String getEntityKey(Class<? extends Entity> clazz){
		return entityClassMap.getKey(clazz);
	}
	
	public Class<? extends Entity> getEntityClass(String key){
		return entityClassMap.getValue(key);
	}
	
}
