package projectmp.util;

import com.badlogic.gdx.utils.Pool;

public class ParticlePool {

	private static ParticlePool instance;

	private ParticlePool() {
	}

	private final Pool<Particle> coordPool = new Pool<Particle>() {

		public Particle newObject() {
			return new Particle();
		}
	};

	public static ParticlePool instance() {
		if (instance == null) {
			instance = new ParticlePool();
			instance.loadResources();
		}
		return instance;
	}

	private void loadResources() {

	}

	public Pool<Particle> getPool() {
		return coordPool;
	}

	public static Particle obtain() {
		return instance().coordPool.obtain();
	}
}
