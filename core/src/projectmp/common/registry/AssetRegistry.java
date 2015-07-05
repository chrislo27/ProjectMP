package projectmp.common.registry;

import java.util.HashMap;
import java.util.Map.Entry;

import projectmp.client.animation.Animation;
import projectmp.common.registry.handler.IAssetLoader;
import projectmp.common.registry.handler.StandardAssetLoader;
import projectmp.common.util.AssetMap;
import projectmp.common.util.GameException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public final class AssetRegistry implements Disposable {

	private static AssetRegistry instance;

	private AssetRegistry() {
	}

	public static AssetRegistry instance() {
		if (instance == null) {
			instance = new AssetRegistry();
			instance.onInstantiate();
		}
		return instance;
	}

	private Array<IAssetLoader> loaders = new Array<>();

	private AssetManager manager = new AssetManager();
	private HashMap<String, Texture> unmanagedTextures = new HashMap<>();
	private HashMap<String, Animation> animations = new HashMap<>();

	private Texture missingTexture;

	private void onInstantiate() {
		addAssetLoader(new StandardAssetLoader());
	}

	public Array<IAssetLoader> getAllAssetLoaders() {
		return loaders;
	}

	public AssetManager getAssetManager() {
		return manager;
	}

	public HashMap<String, Texture> getUnmanagedTextures() {
		return unmanagedTextures;
	}

	public HashMap<String, Animation> getAnimations() {
		return animations;
	}

	public void addAssetLoader(IAssetLoader l) {
		loaders.add(l);

		// add the managed textures to the asset manager
		l.addManagedAssets(manager);
	}

	public void loadManagedAssets(int millis) {
		manager.update(millis);
	}

	public void loadUnmanagedTextures() {
		// put all the textures from the IAssetLoaders, they will be loaded b/c of how they're created
		for (IAssetLoader loader : loaders) {
			loader.addUnmanagedTextures(unmanagedTextures);
		}
	}

	public void loadAnimations() {
		// put all the animations from the IAssetLoaders
		for (IAssetLoader loader : loaders) {
			loader.addUnmanagedAnimations(animations);
		}

		// load the animations
		for (Entry<String, Animation> entry : animations.entrySet()) {
			entry.getValue().load();
		}
	}

	@Override
	public void dispose() {
		manager.dispose();

		for (Entry<String, Texture> entry : unmanagedTextures.entrySet()) {
			entry.getValue().dispose();
		}

		for (Entry<String, Animation> entry : animations.entrySet()) {
			entry.getValue().dispose();
		}

		if(missingTexture != null) missingTexture.dispose();
	}

	public static Texture getMissingTexture() {
		if (instance().missingTexture == null) {
			throw new GameException(
					"Missing texture not created yet; forgot to call #createMissingTexture in "
							+ instance().getClass().getSimpleName());
		} else {
			return instance().missingTexture;
		}
	}

	public static void createMissingTexture() {
		if (instance().missingTexture != null) return;

		// generate missing texture
		Pixmap pix = new Pixmap(32, 32, Format.RGBA8888);

		// pink
		pix.setColor(1, 0, 1, 1);
		pix.drawRectangle(0, 0, pix.getWidth() / 2, pix.getHeight() / 2);
		pix.drawRectangle(pix.getWidth() / 2, pix.getHeight() / 2, pix.getWidth() / 2,
				pix.getHeight() / 2);

		// black
		pix.setColor(0, 0, 0, 1);
		pix.drawRectangle(pix.getWidth() / 2, 0, pix.getWidth() / 2, pix.getHeight() / 2);
		pix.drawRectangle(0, pix.getHeight() / 2, pix.getWidth() / 2, pix.getHeight() / 2);

		// set to texture
		instance().missingTexture = new Texture(pix);

		pix.dispose();
	}

	/**
	 * uses the instance() method and AssetMap key to return a Texture. It will attempt to search the unmanaged textures map first.
	 * @param key
	 * @return the Texture, searching unmanaged textures first or null if none is found
	 */
	public static Texture getTexture(String key) {
		if (instance().getUnmanagedTextures().containsKey(key)) {
			return instance().getUnmanagedTextures().get(key);
		} else {
			if (!AssetMap.containsKey(key)) return getMissingTexture();
			if (!instance().getAssetManager().isLoaded(AssetMap.get(key), Texture.class)) return getMissingTexture();

			return instance().getAssetManager().get(AssetMap.get(key), Texture.class);
		}
	}

	public static Sound getSound(String key) {
		if (!AssetMap.containsKey(key)) return null;
		if (!instance().getAssetManager().isLoaded(AssetMap.get(key), Sound.class)) return null;

		return instance().getAssetManager().get(AssetMap.get(key), Sound.class);
	}

	public static Music getMusic(String key) {
		if (!AssetMap.containsKey(key)) return null;
		if (!instance().getAssetManager().isLoaded(AssetMap.get(key), Music.class)) return null;

		return instance().getAssetManager().get(AssetMap.get(key), Music.class);
	}

	public static Animation getAnimation(String key) {
		return instance().getAnimations().get(key);
	}

}
