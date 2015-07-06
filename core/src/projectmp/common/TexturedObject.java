package projectmp.common;

import java.util.HashMap;

import projectmp.client.animation.Animation;
import projectmp.client.animation.LoopingAnimation;
import projectmp.common.block.Block;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.registry.handler.IAssetLoader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * 
 * Intended to combine the image loading for items and blocks.
 * <br>
 * For blocks, the prefix is "block" and the identifier is chosen in the constructor.
 * <br>
 * For items, the prefix is "item" and the identifier is the unlocalized name.
 *
 */
public class TexturedObject {
	
	String prefix = "";
	String identifier = "";
	
	public TexturedObject(String prefix, String id){
		this.prefix = prefix;
		identifier = id;
	}
	
	public TexturedObject addAnimations(final Animation... args){
		IAssetLoader assetLoader = new IAssetLoader(){
			
			@Override
			public void addUnmanagedAnimations(HashMap<String, Animation> animations) {
				for(int i = 0; i < args.length; i++){
					if(args[i] == null){
						throw new IllegalArgumentException("Arguments in TexturedObject loader cannot be null");
					}
					
					animations.put(prefix + "_" + identifier + "_" + i, args[i]);
				}
			}
			
			// we don't care about the ones below
			
			@Override
			public void addManagedAssets(AssetManager manager) {
			}

			@Override
			public void addUnmanagedTextures(HashMap<String, Texture> textures) {
			}

		};
		AssetRegistry.instance().addAssetLoader(assetLoader);
		
		return this;
	}
	
	/**
	 * Gets an animation from AssetRegistry with the key:
	 * <br>
	 * prefix_identifier_index
	 * @param index
	 * @return
	 */
	public Animation getAnimation(int index){
		return AssetRegistry.getAnimation(prefix + "_" + identifier + "_" + index);
	}
	
	public static Animation newSingleFrame(String path){
		return new LoopingAnimation(1, 1, path, false);
	}

}
