package projectmp.common.block;


public class Blocks {
	
	private static Blocks instance;

	private Blocks() {
	}

	public static Blocks instance() {
		if (instance == null) {
			instance = new Blocks();
			instance.loadResources();
		}
		return instance;
	}

	private void loadResources() {

	}
	
}
