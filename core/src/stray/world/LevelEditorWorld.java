package stray.world;

import stray.Main;
import stray.LevelData.LevelType;


public class LevelEditorWorld extends World{

	public LevelEditorWorld(Main main, int x, int y) {
		super(main, x, y, LevelType.NORMAL);
	}
	
	public LevelEditorWorld(Main main){
		super(main);
	}
	
	@Override
	public void prepare(){
		super.prepare();
		objectives.clear();
	}
	
	@Override
	public int getMeta(int x, int y){
		if(x == -1337 && y == -1337){
			return Main.LEVELEDITOR.defaultmeta;
		}
		return super.getMeta(x, y);
	}

}
