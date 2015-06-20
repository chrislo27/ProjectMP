package projectmp.common.registry;


public class RegistryBase<T> {

	private RegistryMap<T> registry = new RegistryMap<>();
	
	public void register(Class<? extends T> clazz, String name){
		registry.register(clazz, name);
	}
	
	public RegistryMap<T> getRegistry(){
		return registry;
	}
	
}
