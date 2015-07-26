package projectmp.common.energy;

/**
 * This block can connect to other IPowerConnectors like cables
 * <br>
 * Implements a canConnect method.
 *
 */
public interface IPowerConnector {

	public boolean canConnect(int blockFace);
	
}
