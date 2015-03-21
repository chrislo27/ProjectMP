package projectmp.ai;

import projectmp.Main;
import projectmp.entity.Entity;
import projectmp.entity.EntityFlame;
import projectmp.entity.EntityLiving;
import projectmp.util.Direction;

public class AIWhale extends AIDumbEnemy {

	public AIWhale(Entity e) {
		super(e);
	}

	@Override
	public void renderUpdate() {
		super.renderUpdate();
	}

	@Override
	public void tickUpdate() {
		super.tickUpdate();

		if (((EntityLiving) entity).facing == Direction.RIGHT) {
			if (entity.world.getPlayer().x >= entity.x + entity.sizex
					&& Math.abs(entity.world.getPlayer().x - (entity.x + entity.sizex)) < 10f) {
				EntityFlame flame = new EntityFlame(entity.world, entity.x + 8.703125f, entity.y + 3.9375f);
				flame.velox = (entity.world.getPlayer().x - flame.x) * 2f;
				flame.veloy = ((entity.world.getPlayer().y + entity.world.getPlayer().sizex) - flame.y) * 1.25f;

				entity.world.entities.add(flame);
			}
		} else if (((EntityLiving) entity).facing == Direction.LEFT) {
			if (entity.world.getPlayer().x + entity.world.getPlayer().sizex <= entity.x
					&& Math.abs((entity.world.getPlayer().x + entity.world.getPlayer().sizex) - entity.x) < 10f) {
				EntityFlame flame = new EntityFlame(entity.world, entity.x + 0.875f, entity.y + 3.9375f);
				flame.velox = (entity.world.getPlayer().x - flame.x) * 2f;
				flame.veloy = ((entity.world.getPlayer().y + entity.world.getPlayer().sizex) - flame.y) * 1.25f;

				entity.world.entities.add(flame);
			}
		}

	}

}
