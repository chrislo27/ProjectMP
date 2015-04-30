package projectmp.common.inventory;

import projectmp.common.util.MathHelper;

import com.badlogic.gdx.utils.Array;


public class Inventory {

	Array<Slot> slots = new Array<Slot>();
	
	public Slot getSlotAt(float x, float y){
		for(Slot slot : slots){
			if(MathHelper.intersects(x, y, 1, 1, slot.posx, slot.posy, slot.width, slot.height)){
				return slot;
			}
		}
		
		return null;
	}
	
}
