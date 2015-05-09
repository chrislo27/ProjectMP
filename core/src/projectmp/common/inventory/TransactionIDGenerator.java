package projectmp.common.inventory;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;


public class TransactionIDGenerator {
	
	private static Random random = new Random(System.nanoTime());
	
	public static long getNewTransactionID(){
		return random.nextLong() * MathUtils.randomSign();
	}
	
}
