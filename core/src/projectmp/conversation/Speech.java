package projectmp.conversation;

/**
 * holds the name of the speaker and the text
 * 
 *
 */
public class Speech {

	/**
	 * references -> voices, "conv.name." for translator name
	 */
	public String speaker = "N/A";
	public String text = "...";
	public String imagepath = null;
	public String voice = null;

	public Speech(String talker, String t, String img) {
		speaker = talker;
		text = t;
		imagepath = img;
	}

	public Speech(String talker, String t) {
		this(talker, t, null);
	}
	
	public Speech setVoice(String v){
		voice = v;
		return this;
	}
}
