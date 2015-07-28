package projectmp.common.block.droprate;

import projectmp.common.item.Item;
import projectmp.common.item.Items;

import com.badlogic.gdx.math.MathUtils;


public class DropRate {

	public String item = null;
	public int minQuantity = 1;
	public int maxQuantity = 1;
	/**
	 * Drop rate of at least minQuantity if a random float between 0 and 1 is less than this
	 */
	public float minimumChance = 1;
	
	public DropRate(String item, int minAmt, int maxAmt, float minimumChance){
		this.item = item;
		minQuantity = minAmt;
		maxQuantity = maxAmt;
		this.minimumChance = minimumChance;
	}
	
	public Item getItem(){
		return Items.instance().getItem(item);
	}
	
	/**
	 * Returns the quantity to be dropped based on the weights and whatnot
	 * @return
	 */
	public int getRandomQuantity(){
		if(MathUtils.randomBoolean(minimumChance)){
			return MathUtils.random(minQuantity, maxQuantity);
		}else{
			return 0;
		}
	}
}
