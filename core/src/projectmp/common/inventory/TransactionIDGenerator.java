package projectmp.common.inventory;

import com.badlogic.gdx.math.MathUtils;


public class TransactionIDGenerator {
	
	/**
	 * Uses the magic of nanosecond time and some random numbers to make a transaction id
	 * that is essentially guaranteed to never occur again within the same client
	 * <br>
	 * <br>
	 * The formula is:
	 * <br>
	 * <code>(random32bitInt << 32) | (nanoTime & 0xFFFFFFFF)
	 * </code>
	 * 
	 * @return a transaction id
	 */
	public static long getNewTransactionID(){
		// random 32 bit int, bit shifted left by 32
		long random = MathUtils.random(Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1) << 32;
		// OR by nano time which is ANDed 32 times
		long transId = random | (System.nanoTime() & 0xFFFFFFFF);
		
		return transId;
	}
	
}
