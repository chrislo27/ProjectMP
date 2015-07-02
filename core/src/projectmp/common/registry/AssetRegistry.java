package projectmp.common.registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import projectmp.client.animation.Animation;
import projectmp.client.animation.LoopingAnimation;
import projectmp.common.registry.handler.IAssetLoader;
import projectmp.common.registry.handler.StandardAssetLoader;

import com.badlogic.gdx.assets.AssetManager;
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
	}

}
