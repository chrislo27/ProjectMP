package projectmp.client.ui;

import projectmp.common.Main;
import projectmp.common.util.MathHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;


public class TextBox extends Button{

	public TextBox(int x, int y, int width, int height, String text) {
		super(x, y, width, height, text);
	}
	
	boolean clicked = false;
	private long clickedTime = 0;
	
	@Override
	public void render(Main main){
		imageRender(main, "guibgtext");
		main.font.setColor(1, 1, 1, 1);
		main.font.draw(main.batch, text, x + 8, y + (height / 2) + (main.font.getBounds(text).height / 2));
		if(clicked && MathHelper.getNumberFromTime(System.currentTimeMillis() - clickedTime, 1f) <= 0.5f){
			main.font.draw(main.batch, "|", x + 8 + main.font.getBounds(text).width, y + (height / 2) + (main.font.getBounds(text).height / 2));
		}
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			if(!Container.mouseIn(this)){
				setFocus(false);
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			setFocus(false);
		}else if(Gdx.input.isKeyJustPressed(Keys.BACKSPACE)){
			if(text.length() > 0){
				text = text.substring(0, text.length() - 1);
			}
		}
	}
	
	@Override
	public boolean onLeftClick(){
		setFocus(true);
		return true;
	}
	
	public void setFocus(boolean f){
		if(f){
			if(!clicked){
				clickedTime = System.currentTimeMillis();
			}
			clicked = true;
		}else{
			clicked = false;
		}
	}
	
	@Override
	public boolean onKeyTyped(char key){
		if(!clicked) return false;
		if(!Character.isLetterOrDigit(key)) return false;
		
		text += key;
		return true;
	}

}
