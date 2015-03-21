package stray.util;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import stray.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;


public class ScreenshotFactory {

	private ScreenshotFactory(){}
	
	public static void saveScreenshot(){
        try{
            FileHandle fh;
            String date = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
			date.trim();
            do{
                fh = new FileHandle("screenshots/screenshot_" + date + ".png");
            }while (fh.exists());
            Pixmap pixmap = getScreenshot(0, 0, Settings.DEFAULT_WIDTH, Gdx.graphics.getHeight(), true);
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
        }catch (Exception e){           
        }
    }

    public static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

        if (yDown) {
            // Flip the pixmap upside down
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        }

        return pixmap;
    }
}
