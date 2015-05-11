package projectmp.common.inventory;

import projectmp.common.util.MathHelper;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;


public class Inventory {

	public static final int PLAYER_SLOTS = 10;
	
	ItemStack[] items = new ItemStack[1];
	int maxCapacity = 1;
	
	public Inventory(){
		
	}
	
	/**
	 * sets max capacity AND re-inits itemstack array
	 * @param cap
	 * @return
	 */
	public Inventory setMaxCapacity(int cap){
		maxCapacity = cap;
		items = new ItemStack[cap];
		
		return this;
	}
	
}
