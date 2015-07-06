package projectmp.client;

import projectmp.common.Main;
import projectmp.common.Settings;
import projectmp.common.block.Block;
import projectmp.common.block.Blocks;
import projectmp.common.item.Item;
import projectmp.common.item.Items;
import projectmp.common.registry.AssetRegistry;
import projectmp.common.util.AssetLogger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;

public class AssetLoadingScreen extends MiscLoadingScreen {

	public AssetLoadingScreen(Main m) {
		super(m);
		AssetRegistry.instance().getAssetManager().setLogger(output);
	}

	private AssetLogger output = new AssetLogger("assetoutput", Logger.DEBUG);

	private long startms = 0;

	private boolean waitedAFrame = false;

	@Override
	public void render(float delta) {
		AssetManager manager = AssetRegistry.instance().getAssetManager();
		
		AssetRegistry.instance().loadManagedAssets(((int) (1000f / Main.MAX_FPS)));
		do {
			if (AssetRegistry.instance().finishedLoading()) {
				if (!waitedAFrame) {
					waitedAFrame = true;
					break;
				}

				Main.logger.info("Finished loading all managed assets, took "
						+ (System.currentTimeMillis() - startms) + " ms");

				main.setScreen(Main.MAINMENU);
			}
		} while (false);

		super.render(delta);

		main.batch.begin();
		main.batch.setColor(1, 1, 1, 1);
		Main.fillRect(main.batch, Settings.DEFAULT_WIDTH / 2 - 128, Gdx.graphics.getHeight() / 2 - 10,
				256 * manager.getProgress(), 20);

		Main.fillRect(main.batch, Settings.DEFAULT_WIDTH / 2 - 130, Gdx.graphics.getHeight() / 2 - 12, 260, 1);
		Main.fillRect(main.batch, Settings.DEFAULT_WIDTH / 2 - 130, Gdx.graphics.getHeight() / 2 + 11, 260, 1);

		Main.fillRect(main.batch, Settings.DEFAULT_WIDTH / 2 - 130, Gdx.graphics.getHeight() / 2 - 12, 1, 24);
		Main.fillRect(main.batch, Settings.DEFAULT_WIDTH / 2 + 132, Gdx.graphics.getHeight() / 2 - 12, 1, 24);

		if (manager.getAssetNames().size > 0) {
			main.drawTextBg(main.font, output.getLastMsg(),
					Settings.DEFAULT_WIDTH / 2
							- (main.font.getBounds(output.getLastMsg()).width / 2),
					Gdx.graphics.getHeight() / 2 - 35);
		}
		String percent = String.format("%.0f", (manager.getProgress() * 100f)) + "%";
		main.drawTextBg(main.font, percent,
				Settings.DEFAULT_WIDTH / 2 - (main.font.getBounds(percent).width / 2),
				Gdx.graphics.getHeight() / 2 - 60);

		main.batch.end();
	}

	@Override
	public void tickUpdate() {

	}

	@Override
	public void show() {
		startms = System.currentTimeMillis();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {

	}

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void renderUpdate() {
	}

}
