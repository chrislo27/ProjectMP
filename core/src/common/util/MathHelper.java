package common.util;

import com.badlogic.gdx.math.MathUtils;

public class MathHelper {

	private MathHelper() {
	};

	public static double getScaleFactor(float iMasterSize, float iTargetSize) {
		double dScale = 1;
		if (iMasterSize > iTargetSize) {
			dScale = (double) iTargetSize / (double) iMasterSize;
		} else {
			dScale = (double) iTargetSize / (double) iMasterSize;
		}
		return dScale;
	}

	public static float calcRotationAngleInDegrees(float x, float y, float tx, float ty) {
		float theta = MathUtils.atan2(tx - x, ty - y);

		float angle = theta * MathUtils.radiansToDegrees;

		if (angle < 0) {
			angle += 360;
		}
		angle += 180;

		return angle;
	}

	public static float calcRotationAngleInRadians(float x, float y, float tx, float ty) {
		return calcRotationAngleInDegrees(x, y, tx, ty) * MathUtils.degreesToRadians;
	}

	public static double calcRadiansDiff(float x, float y, float tx, float ty) {
		double d = calcRotationAngleInDegrees(x, y, tx, ty);
		d -= 90;
		d %= 360;
		return Math.toRadians(d);
	}

	public static float timePulse(float num) {
		return ((num > 0.5f ? (0.5f - (num - 0.5f)) : num)) - MathUtils.clamp(0.50000001f, 1f, 0.5f);
	}

	public static double calcDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	public static double clamp(double val, double min, double max) {
	    return Math.max(min, Math.min(max, val));
	}

	/**
	 * get a number from 0, 1 based on time
	 * 
	 * @return
	 */
	public static float getNumberFromTime() {
		return getNumberFromTime(System.currentTimeMillis(), 1);
	}

	public static float getNumberFromTime(float seconds) {
		return getNumberFromTime(System.currentTimeMillis(), seconds);
	}
	
	public static float getNumberFromTime(long time, float seconds) {
		if(seconds == 0) throw new IllegalArgumentException("Seconds cannot be zero!");
		return ((time % Math.round((seconds * 1000))) / (seconds * 1000f));
	}
	
	public static float clampNumberFromTime(long ms, float seconds){
		float f = getNumberFromTime(ms, seconds);
		if(f >= 0.5f){
			return 1f - f;
		}else return f;
	}
	
	public static float clampNumberFromTime(float sec){
		return clampNumberFromTime(System.currentTimeMillis(), sec);
	}

	public static int getNthDigit(int number, int n) {
		int base = 10;
		return (int) ((number / Math.pow(base, n - 1)) % base);
	}

	public static int getNthDigit(long number, int n) {
		int base = 10;
		return (int) ((number / Math.pow(base, n - 1)) % base);
	}

	// original x, y, new x, y
	public static double getScaleFactorToFit(float ox, float oy, float nx, float ny) {
		double dScale = 1d;
		double dScaleWidth = getScaleFactor(ox, nx);
		double dScaleHeight = getScaleFactor(oy, ny);

		dScale = Math.min(dScaleHeight, dScaleWidth);

		return dScale;
	}

	public static boolean checkPowerOfTwo(int number) {
		if (number <= 0) {
			throw new IllegalArgumentException("Number is less than zero: " + number);
		}
		return ((number & (number - 1)) == 0);
	}

//	public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
//		if (val.compareTo(min) < 0) return min;
//		else if (val.compareTo(max) > 0) return max;
//		else return val;
//	}

	public static float distanceSquared(float x, float y, float x2, float y2) {
		return (x2 - x) * (x2 - x) + (y2 - y) * (y2 - y);
	}

	public static float lightingCalc(int l) {
		return 1.0f - ((float) (logOfBase(15, l)));
	}

	public static double logOfBase(int base, int num) {
		return Math.log(num) / Math.log(base);
	}

	public static boolean isOneOfThem(int check, int[] these) {
		for (int i : these) {
			if (check == i) return true;
		}
		return false;
	}

	public static boolean isOneOfThem(int check, int com) {
		return check == com;
	}
	
	public static float getJumpVelo(double gravity, double distance){
		return (float) (gravity * Math.sqrt((2 * distance) / gravity));
	}

	public static boolean intersects(double oldx, double oldy, double oldwidth, double oldheight, double oldx2,
			double oldy2, double oldwidth2, double oldheight2) {

		double x, y, width, height, x2, y2, width2, height2;

		x = oldx;
		y = oldy;
		width = oldwidth;
		height = oldheight;
		if (oldwidth < 0) {
			width = oldx + oldwidth;
			x -= Math.abs(oldwidth);
		}
		if (oldheight < 0) {
			height = oldy + oldheight;
			y -= Math.abs(oldheight);
		}
		x2 = oldx2;
		y2 = oldy2;
		width2 = oldwidth2;
		height2 = oldheight2;
		if (oldwidth2 < 0) {
			width2 = oldx2 + oldwidth2;
			x2 -= Math.abs(oldwidth2);
		}
		if (oldheight2 < 0) {
			height2 = oldy2 + oldheight2;
			y -= Math.abs(oldheight2);
		}

		// System.out.print(x + ", " + y + ":" + width + ", " + height + " = ");
		// System.out.println(x2 + ", " + y2 + ":" + width2 + ", " + height2);

		if ((x > (x2 + width2))) {
			// System.out.println("x > ");
			return false;
		}
		if (((x + width) < x2)) {
			// System.out.println("x2 >");
			return false;
		}

		if ((y > (y2 + height2))) {
			// System.out.println("y >");
			return false;
		}
		if (((y + height) < y2)) {
			// System.out.println("y2 >");
			return false;
		}

		return true;
	}

}
