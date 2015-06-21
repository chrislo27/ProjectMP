package projectmp.common.registry.instantiator;

import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityLiving;
import projectmp.common.entity.EntityPlayer;


public class EntityRegistry extends RegistryBase<Entity>{

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
	
	private void loadResources() {
		register(EntityLiving.class, "entityLiving");
		register(EntityPlayer.class, "player");
	}
	
}
