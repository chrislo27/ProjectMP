package projectmp.common.inventory;

import projectmp.common.util.MathHelper;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;


public class Inventory {

	public static final int PLAYER_SLOTS = 10;
	
	ItemStack[] items;
	int maxCapacity;
	
	public Inventory(final int maxCap){
		maxCapacity = maxCap;
		items = new ItemStack[maxCap];
	}
	
}
