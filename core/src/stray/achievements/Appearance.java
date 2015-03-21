package stray.achievements;

public class Appearance {

	public Appearance(Achievement a) {
		this.a = a;
	}

	public Achievement a;
	public int time = startingTime;
	public float y = 0;

	public static final int startingTime = 7 * 20 + 20;
}
