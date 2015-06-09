package projectmp.common.inventory;

import projectmp.common.util.sidedictation.Side;
import projectmp.common.util.sidedictation.SideOnly;

@SideOnly(Side.CLIENT)
public class TransactionIDGenerator {

	private static long currentID = System.nanoTime();

	public static long getNewTransactionID() {
		return currentID++;
	}

}
