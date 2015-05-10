package projectmp.common.inventory;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;


public class TransactionIDGenerator {
	
	private static long currentID = System.nanoTime();
	
	public static long getNewTransactionID(){
		return currentID++;
	}
	
}
