package projectmp.blocks;

import projectmp.Levels;
import projectmp.Main;
import projectmp.transition.FadeIn;
import projectmp.util.ParticlePool;
import projectmp.world.World;

public class BlockExitPortal extends Block {

	public BlockExitPortal(String string) {
		super(string);
	}

	public static final int AMOUNT_REQUIRED = 5;

	@Override
	public void render(World world, int x, int y) {
		super.render(world, x, y);
	}

	@Override
	public void tickUpdate(World world, int x, int y) {
		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			if (!world.global.getString("completedLevel").equals("done!")) {
				world.global.setString("completedLevel", "done!");
				world.completeObjective("complete_level", false);
				world.completeObjective(null, false);

				if (world.main.getScreen() != Main.GAME) return;
				save(world);
				Main.LEVELSELECT.moveNext();
				world.main.transition(new FadeIn(), null, Main.RESULTS.setData(world.levelfile,
						Levels.instance().getNumFromLevelFile(world.levelfile), world.deaths));
			}
		}

	}

	@Override
	public boolean isRenderedFront() {
		return false;
	}

	private void save(World world) {
		long lasttime = System.currentTimeMillis() - world.msTime;

		if (lasttime < world.main.progress.getLong(world.levelfile + "-besttime",
				Long.MAX_VALUE - 1)) {
			world.main.progress.putLong(world.levelfile + "-besttime", lasttime);
		}

		world.main.progress.putLong(world.levelfile + "-latesttime", lasttime);

		if (world.main.progress.getInteger("rightmostlevel", 0) == Main.LEVELSELECT.getCurrent()) {
			world.main.progress.putInteger("rightmostlevel",
					world.main.progress.getInteger("rightmostlevel", 0) + 1);

		}

		world.main.progress.flush();
	}

}
