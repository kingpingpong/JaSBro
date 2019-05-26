package jasbro.gui.objects.menus;

import jasbro.gui.objects.menus.actions.SlotLoadAction;
import jasbro.gui.objects.menus.actions.SlotSaveAction;
import jasbro.texts.TextUtil;

import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MainMenuBar extends JMenuBar {
	
	private MyInfoPanel statusInfo;
	private JMenu gameMenu;
	
	public MainMenuBar() {
		
		// Game menu
		gameMenu = new GameMenu();
		add(gameMenu);
		
		// Options menu
		add(new OptionMenu());
		
		add(Box.createGlue());
		
		// Quicksave button
		JMenuItem quickSaveButton = new JMenuItem(new SlotSaveAction(TextUtil.t("ui.quicksave"), -1));
		quickSaveButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		add(quickSaveButton);
		
		// Quickload button
		JMenuItem quickLoadButton = new JMenuItem(new SlotLoadAction(TextUtil.t("ui.quickload"), -1));
		quickLoadButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		add(quickLoadButton);
		
		add(Box.createGlue());
		add(Box.createGlue());
		add(Box.createGlue());
		add(Box.createGlue());
		add(Box.createGlue());
		add(Box.createGlue());
		add(Box.createGlue());
		
		statusInfo = new MyInfoPanel();
		add(statusInfo);
	}
	
	//TODO Better solution needed!
	public void rebuildSaveMenu() {
		gameMenu.remove(3);
		gameMenu.add(new SaveMenu(), 3);
	}
	
	//TODO Better solution needed!
	public void rebuildLoadMenu() {
		gameMenu.remove(4);
		gameMenu.add(new LoadMenu(), 4);
	}
	
	public void updateStatusInfo() {
		statusInfo.update();
	}
}