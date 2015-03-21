package projectmp.conversation;

import projectmp.Main;
import projectmp.Translator;
import projectmp.util.AssetMap;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 * holds the list of speeches and the number it is currently on
 * 
 *
 */
public class Conversation {

	public int speechiter = 0;
	protected Array<Speech> speeches;
	private String lastSpeaker = null;

	public Conversation(Speech[] speeches) {
		reset();
		this.speeches = new Array<Speech>(speeches);
		this.speeches.ordered = true;
	}

	public Speech getCurrent() {
		return speeches.get(speechiter);
	}

	public Speech getNext() {
		if (speechiter + 1 < speeches.size) {
			return speeches.get(speechiter + 1);
		}
		return null;
	}

	public void reset() {
		speechiter = 0;
		lastSpeaker = null;
	}

	/**
	 * 
	 * @return true if finished (will reset itself), false otherwise
	 */
	public boolean next() {
		if (++speechiter >= speeches.size) {
			reset();
			return true;
		}
		return false;
	}

	public void talk(Main main, float volume) {
		if (getCurrent().speaker == null) return;
		if (getCurrent().voice != null) {
			main.manager.get(AssetMap.get("voice-" + getCurrent().voice),
					Sound.class).play(volume);
			lastSpeaker = getCurrent().speaker;
		}
	}
}
