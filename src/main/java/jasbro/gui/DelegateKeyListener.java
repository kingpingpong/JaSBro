package jasbro.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DelegateKeyListener implements KeyListener {
	
	@Override
	public void keyPressed(KeyEvent e) {
		e.getComponent().getParent().dispatchEvent(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		e.getComponent().getParent().dispatchEvent(e);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		e.getComponent().getParent().dispatchEvent(e);
	}
	
}