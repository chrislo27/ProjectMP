package projectmp.common.registry.instantiator;

/**
 * Registries pair a class and a string key for future instantiation.
 * 
 *
 * @param <T>
 */
public class RegistryBase<T> {

	private RegistryMap<T> registry = new RegistryMap<>();
	
	public void register(Class<? extends T> clazz, String name){
		registry.register(clazz, name);
	}
	
	public RegistryMap<T> getRegistry(){
		return registry;
	}
	
}
