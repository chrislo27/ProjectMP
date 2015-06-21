package projectmp.common.factory;


public abstract class Blueprint<T> {

	public abstract T manufacture(String key);
	
}
