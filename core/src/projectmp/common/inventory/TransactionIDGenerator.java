package projectmp.common.inventory;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;


public class TransactionIDGenerator {
	
	private static Random random = new Random();
	
	/**
	 * Returns a random transaction ID that's essentially never going to happen again
	 * <br>
	 * It sets the seed to the current nanosecond time, then returns <br>
	 * <code>
	 * nextLong() * randomSign
	 * </code>
	 * <p>
	 * 
	 * The only way to make it return a value again is to change the computer clock
	 * back and somehow re-call this method at the exact nanosecond.
	 * 
	 * @return random transaction ID
	 */
	public static long getNewTransactionID(){
		random.setSeed(System.nanoTime());
		
		return random.nextLong() * MathUtils.randomSign();
	}
	
}
