package projectmp.client.transition;

import projectmp.common.Main;

public interface Transition {

	public boolean finished();

	public void render(Main main);

	public void tickUpdate(Main main);
}
