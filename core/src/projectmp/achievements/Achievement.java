package projectmp.achievements;

public class Achievement {

	public Achievement(String data) {
		this.data = data;
	}

	/**
	 * used in localization and ID
	 */
	public String data = "missingno";

	public boolean hidden = false;
	public boolean special = false;

	public Achievement setHidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public Achievement setSpecial(boolean special) {
		this.special = special;
		return this;
	}

}
