package projectmp.common.registry;

import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityLiving;
import projectmp.common.entity.EntityPlayer;


public class EntityRegistry {

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

	private RegistryMap<Entity> entityRegistry = new RegistryMap<>();
	
	private void loadResources() {
		registerEntity(EntityLiving.class, "entityLiving");
		registerEntity(EntityPlayer.class, "player");
	}
	
	public void registerEntity(Class<? extends Entity> clazz, String name) {
		entityRegistry.register(clazz, name);
	}
	
	public RegistryMap<Entity> getEntityRegistry() {
		return entityRegistry;
	}
	
}
