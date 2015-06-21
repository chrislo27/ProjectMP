package projectmp.common.registry.factory;


public abstract class Blueprint<T> {

	public abstract T manufacture(String key);
	
}
