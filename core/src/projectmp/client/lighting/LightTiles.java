package projectmp.client.lighting;

import com.badlogic.gdx.graphics.Color;


public class LightTiles {

	int sizex, sizey;
	
	byte[][] brightness;
	int[][] color;
	
	public LightTiles(int sx, int sy){
		sizex = sx;
		sizey = sy;
		
		brightness = new byte[sizex][sizey];
		color = new int[sizex][sizey];
		
		for(int x = 0; x < sizex; x++){
			for(int y = 0; y < sizey; y++){
				brightness[x][y] = 0;
				color[x][y] = Color.rgb888(0, 0, 0);
			}
		}
	}
	
}
