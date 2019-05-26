package jasbro.gui.pages;

import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;

public class GameOverScreen extends MyImage implements MessageInterface {
	public GameOverScreen() {
		setBackgroundImage(new ImageData("images/backgrounds/gameOver.jpg"));
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public boolean isPriorityMessage() {
		return true;
	}
	
	@Override
	public void setMessageGroupObject(Object charcterGroupObject) {
	}
	
	@Override
	public Object getMessageGroupObject() {
		return null;
	}
}