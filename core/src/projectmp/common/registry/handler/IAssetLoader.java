package projectmp.common.registry.handler;

import java.util.HashMap;

import projectmp.client.animation.Animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public interface IAssetLoader {
	
	/**
	 * puts the assets to be loaded in the AssetManager
	 * @param manager
	 */
	public void addManagedAssets(AssetManager manager);
	
	/**
	 * immediately loads unmanaged textures
	 * @param textures
	 */
	public void addUnmanagedTextures(HashMap<String, Texture> textures);
	
	/**
	 * puts the animations to be loaded in the map
	 * @param animations
	 */
	public void addUnmanagedAnimations(HashMap<String, Animation> animations);
	
}
