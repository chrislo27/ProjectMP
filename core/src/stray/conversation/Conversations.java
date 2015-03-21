package stray.conversation;

import java.util.HashMap;

import stray.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * singleton full of conversations
 * 
 *
 */
public class Conversations {

	private static Conversations instance;

	private Conversations() {
	}

	public static Conversations instance() {
		if (instance == null) {
			instance = new Conversations();
			instance.loadResources();
		}
		return instance;
	}

	public HashMap<String, Conversation> convs;

	private void loadResources() {
		convs = new HashMap<String, Conversation>();
		
		long timeTaken = System.currentTimeMillis();

		FileHandle h = Gdx.files.internal("localization/conversation.xml");
		XmlReader reader = new XmlReader();
		Element root = reader.parse(h.readString());
		Array<Element> elements = root.getChildrenByName("conversation");

		for (Element e : elements) { // for each conversation
			Array<Speech> speeches = new Array<Speech>();

			// look in their speech children
			for (Element spe : e.getChildrenByName("speech")) {
				String speaker = spe.getAttribute("speaker", null);
				String cutscenepic = spe.getAttribute("cutscene", null);
				String voice = spe.getAttribute("voice", null);
				String text = spe.getAttribute("text", "");
				
				if(speaker != null) if(speaker.equals("")) speaker = null;
				if(cutscenepic != null) if(cutscenepic.equals("")) cutscenepic = null;

//				Main.logger.info("Adding speech for " + e.getAttribute("id") + ": speaker: "
//						+ speaker + ", text: " + text + ", cutscene: " + cutscenepic + ", voice: "
//						+ voice);

				speeches.add(new Speech(speaker, text, cutscenepic).setVoice(voice));
			}
			convs.put(e.getAttribute("id"),
					new Conversation((Speech[]) speeches.toArray(Speech.class)));
		}
		
		Main.logger.info("Finished loading conversation file, took " + (System.currentTimeMillis() - timeTaken) + " ms");
	}
}
