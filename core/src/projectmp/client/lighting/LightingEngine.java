package projectmp.client.lighting;

import projectmp.common.Main;
import projectmp.common.world.World;

import com.badlogic.gdx.math.MathUtils;


public class LightingEngine {

	private World world;
	private int sizex = 1;
	private int sizey = 1;
	
	private byte[][] brightness;
	private byte[][] temp;
	
	public LightingEngine(World world){
		this.world = world;
		sizex = world.sizex;
		sizey = world.sizey;

		brightness = new byte[sizex][sizey];
		temp = new byte[sizex][sizey];
	}
	
	public void resetLighting(int originx, int originy, int width, int height){
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);
		
		for(int x = originx; x < width; x++){
			for(int y = originy; y < height; y++){
				setBrightness((byte) 0, x, y);
			}
		}
		
	}
	
	public void updateLighting(int originx, int originy, int width, int height){
		originx = MathUtils.clamp(originx, 0, sizex);
		originy = MathUtils.clamp(originy, 0, sizey);
		width = MathUtils.clamp(width, 0, sizex);
		height = MathUtils.clamp(height, 0, sizey);
		
		copyToTemp();
		
		for(int x = originx; x < width; x++){
			for(int y = originy; y < height; y++){
				if(getTempBrightness(x, y) > 0){
					recursiveLight(x, y, (byte) (getBrightness(x, y) + 1));
				}
			}
		}
	}
	
	private void copyToTemp(){
		for(int x = 0; x < sizex; x++){
			for(int y = 0; y < sizey; y++){
				temp[x][y] = brightness[x][y];
			}
		}
	}
	
	private void recursiveLight(int x, int y, byte bright){
		if(bright <= 0){
			return;
		}
		if (getBrightness(x, y) >= bright && bright > 0){
			return;
		}
		if (x < 0 || y < 0 || x + 1 >= sizex || y + 1 >= sizey){
			return;
		}

		setBrightness(bright, x, y);
		bright = (byte) Math.max(bright - world.getBlock(x, y).lightSubtraction(world, x, y), 0);
		
		recursiveLight(x - 1, y, bright);
		recursiveLight(x, y - 1, bright);
		recursiveLight(x + 1, y, bright);
		recursiveLight(x, y + 1, bright);
	}
	
	public void setBrightness(byte l, int x, int y){
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		brightness[x][y] = l;
	}
	
	public byte getBrightness(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return 0;
		return brightness[x][y];
	}
	
	public byte getTempBrightness(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return 0;
		return temp[x][y];
	}
	
}
