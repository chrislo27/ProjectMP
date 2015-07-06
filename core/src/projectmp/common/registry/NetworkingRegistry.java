package projectmp.common.registry;

import projectmp.common.registry.handler.INetworkHandler;
import projectmp.common.registry.handler.StockNetworkHandler;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;


public class NetworkingRegistry {
	
	private static NetworkingRegistry instance;

	private NetworkingRegistry() {
	}

	public static NetworkingRegistry instance() {
		if (instance == null) {
			instance = new NetworkingRegistry();
			instance.loadResources();
		}
		return instance;
	}

	Array<INetworkHandler> handlers = new Array<>();
	
	private void loadResources() {
		registerHandler(new StockNetworkHandler());
	}
	
	public void registerHandler(INetworkHandler handler){
		handlers.add(handler);
	}
	
	/**
	 * registers all the things into the given Kryo instance
	 */
	public void registerClasses(Kryo kryo){
		INetworkHandler handler = null;
		
		for(int i = 0; i < handlers.size; i++){
			handler = handlers.get(i);
			
			handler.registerClasses(kryo);
		}
	}
	
}
