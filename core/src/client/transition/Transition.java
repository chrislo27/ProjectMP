package client.transition;

import common.Main;

public interface Transition {

	public boolean finished();

	public void render(Main main);

	public void tickUpdate(Main main);
}
