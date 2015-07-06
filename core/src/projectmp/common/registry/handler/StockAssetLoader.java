package projectmp.common.registry.handler;

import java.util.HashMap;

import projectmp.client.animation.Animation;
import projectmp.client.animation.LoopingAnimation;
import projectmp.common.util.AssetMap;
import projectmp.common.util.render.Gears;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class StockAssetLoader implements IAssetLoader {

	@Override
	public void addManagedAssets(AssetManager manager) {
		// missing
		manager.load(AssetMap.add("missingtexture", "images/missing.png"), Texture.class);

		// ui
		manager.load(AssetMap.add("spacekraken", "images/ui/misc.png"), Texture.class);
		manager.load(AssetMap.add("achievementui", "images/ui/achievement.png"), Texture.class);
		manager.load(AssetMap.add("guilanguage", "images/ui/button/language.png"), Texture.class);
		manager.load(AssetMap.add("guisettings", "images/ui/button/settings.png"), Texture.class);
		manager.load(AssetMap.add("guibg", "images/ui/button/bg.png"), Texture.class);
		manager.load(AssetMap.add("guibgtext", "images/ui/button/bg-textfield.png"), Texture.class);
		manager.load(AssetMap.add("guislider", "images/ui/button/slider.png"), Texture.class);
		manager.load(AssetMap.add("guisliderarrow", "images/ui/button/sliderarrow.png"),
				Texture.class);
		manager.load(AssetMap.add("guiexit", "images/ui/button/exit.png"), Texture.class);
		manager.load(AssetMap.add("guiback", "images/ui/button/backbutton.png"), Texture.class);
		manager.load(AssetMap.add("guibgfalse", "images/ui/button/bgfalse.png"), Texture.class);
		manager.load(AssetMap.add("guibgtrue", "images/ui/button/bgtrue.png"), Texture.class);
		manager.load(AssetMap.add("detectionarrow", "images/ui/detection.png"), Texture.class);
		manager.load(AssetMap.add("guilevelselect", "images/ui/button/levelselect.png"),
				Texture.class);
		manager.load(AssetMap.add("guinextlevel", "images/ui/button/nextlevel.png"), Texture.class);
		manager.load(AssetMap.add("guiretry", "images/ui/button/retry.png"), Texture.class);
		manager.load(AssetMap.add("playerdirectionarrow", "images/ui/player-arrow.png"),
				Texture.class);
		manager.load(AssetMap.add("invslot", "images/ui/inventory/itemslot.png"), Texture.class);

		// particle
		manager.load(AssetMap.add("money", "images/particle/money.png"), Texture.class);
		manager.load(AssetMap.add("checkpoint", "images/particle/checkpoint.png"), Texture.class);
		manager.load(AssetMap.add("poof", "images/particle/poof.png"), Texture.class);
		manager.load(AssetMap.add("sparkle", "images/particle/sparkle.png"), Texture.class);
		manager.load(AssetMap.add("arrowup", "images/particle/arrow/up.png"), Texture.class);
		manager.load(AssetMap.add("arrowdown", "images/particle/arrow/down.png"), Texture.class);
		manager.load(AssetMap.add("arrowleft", "images/particle/arrow/left.png"), Texture.class);
		manager.load(AssetMap.add("arrowright", "images/particle/arrow/right.png"), Texture.class);
		manager.load(AssetMap.add("arrowcentre", "images/particle/arrow/centre.png"), Texture.class);
		manager.load(AssetMap.add("lasercube", "images/particle/lasercube.png"), Texture.class);
		manager.load(AssetMap.add("particlepower", "images/particle/power.png"), Texture.class);
		manager.load(AssetMap.add("magnetglow", "images/particle/magnetglow.png"), Texture.class);
		manager.load(AssetMap.add("airwhoosh", "images/particle/airwhoosh.png"), Texture.class);
		manager.load(AssetMap.add("particlecircle", "images/particle/circle.png"), Texture.class);
		manager.load(AssetMap.add("particlestar", "images/particle/star.png"), Texture.class);
		manager.load(AssetMap.add("particleshockwave", "images/particle/explosionshockwave.png"),
				Texture.class);
		manager.load(AssetMap.add("particleflash0", "images/particle/flash0.png"), Texture.class);
		manager.load(AssetMap.add("particleflash1", "images/particle/flash1.png"), Texture.class);
		manager.load(AssetMap.add("particleflash2", "images/particle/flash2.png"), Texture.class);
		manager.load(AssetMap.add("particleflash3", "images/particle/flash3.png"), Texture.class);
		manager.load(AssetMap.add("particleflame0", "images/particle/expflame0.png"), Texture.class);
		manager.load(AssetMap.add("particleflame1", "images/particle/expflame1.png"), Texture.class);
		manager.load(AssetMap.add("particleflame2", "images/particle/expflame2.png"), Texture.class);
		manager.load(AssetMap.add("particleflame3", "images/particle/expflame3.png"), Texture.class);
		manager.load(AssetMap.add("teleporterring", "images/particle/teleporterring.png"),
				Texture.class);

		// effects
		manager.load(AssetMap.add("effecticonblindness", "images/ui/effect/icon/blindness.png"),
				Texture.class);
		manager.load(AssetMap.add("effectoverlayblindness", "images/ui/effect/blindness.png"),
				Texture.class);

		// entities
		manager.load(AssetMap.add("player", "images/entity/player/player.png"), Texture.class);

		// misc
		manager.load(AssetMap.add("vignette", "images/ui/vignette.png"), Texture.class);
		manager.load(AssetMap.add("voidend", "images/voidend.png"), Texture.class);
		manager.load(AssetMap.add("circlegradient", "images/circlegradient.png"), Texture.class);
		manager.load(AssetMap.add("stunhalo", "images/stunhalo.png"), Texture.class);
		manager.load(AssetMap.add("featheredcircle", "images/featheredcircle.png"), Texture.class);

		// background related
		manager.load(AssetMap.add("starrysky", "images/starrysky.png"), Texture.class);
		manager.load(AssetMap.add("celestialbody_moon", "images/celestialbodies/moon.png"),
				Texture.class);
		manager.load(AssetMap.add("celestialbody_sun", "images/celestialbodies/sun.png"),
				Texture.class);

		// sfx

		// music

	}

	@Override
	public void addUnmanagedTextures(HashMap<String, Texture> textures) {
		// misc

		// unmanaged textures
		textures.put("gear", new Texture("images/gear.png"));

	}

	@Override
	public void addUnmanagedAnimations(HashMap<String, Animation> animations) {
		// animations
		animations.put("shine", new LoopingAnimation(0.1f, 20, "images/items/shine/shine.png",
				false));
		animations.put("fire-hud", new LoopingAnimation(0.05f, 8, "images/ui/fire-hudnomiddle.png",
				true).setRegionTile(864, 468).setVertical(false));
	}

}
