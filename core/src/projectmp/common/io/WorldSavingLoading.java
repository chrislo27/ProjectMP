package projectmp.common.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import projectmp.server.player.ServerPlayer;

import com.badlogic.gdx.utils.Array;

public final class WorldSavingLoading {

	private WorldSavingLoading() {
	}

	public static void saveBytes(byte[] world, File location) throws IOException {
		FileOutputStream fos = new FileOutputStream(location);
		GZIPOutputStream gzipstream = new GZIPOutputStream(fos);

		gzipstream.write(world);

		gzipstream.close();
		fos.close();
	}

	public static byte[] loadBytes(File location) throws IOException {
		FileInputStream fis = new FileInputStream(location);
		GZIPInputStream gzipstream = new GZIPInputStream(fis);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2048);
		byte[] buffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = gzipstream.read(buffer)) > 0) {
			byteStream.write(buffer, 0, bytesRead);
		}

		gzipstream.close();
		fis.close();

		return byteStream.toByteArray();
	}

}
