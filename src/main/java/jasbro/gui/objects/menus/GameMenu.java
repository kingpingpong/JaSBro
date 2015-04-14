package jasbro.gui.objects.menus;

import jasbro.Jasbro;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class GameMenu extends JMenu {
	public GameMenu () {
		super (TextUtil.t("ui.menu"));
		setMnemonic(KeyEvent.VK_ALT);
		
		//Main menu button
        JMenuItem mainMenuButton = new JMenuItem(new MainMenuAction());
        add(mainMenuButton);
		
		// New game button
		JMenuItem newGameButton = new JMenuItem (new NewGameAction()); 
		newGameButton.setMnemonic (KeyEvent.VK_N);
		newGameButton.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		add (newGameButton);
		
		addSeparator();
		
		// Save menu
		add (new SaveMenu ());
		
		// Load menu
		add (new LoadMenu ());

		addSeparator();
		
		// Exit button
		JMenuItem exitButton = new JMenuItem (new ExitAction ());
		add (exitButton);
	}
	
    private class MainMenuAction extends AbstractAction {

        public MainMenuAction() {
            super(TextUtil.t("ui.mainmenu"));
        }

        public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().getGui().showMainMenu();
        }
    }
	
	private class NewGameAction extends AbstractAction {
		
		public NewGameAction () {
			super (TextUtil.t("ui.newgame"));
		}
		
		public void actionPerformed (ActionEvent e) {
			Jasbro.getInstance().getGui().showStartScreen();
		}
	}
	
	private class ExitAction extends AbstractAction {
		public ExitAction () {
			super (TextUtil.t("ui.quit"));
		}

		public void actionPerformed (ActionEvent e) {
			JFrame frame = Jasbro.getInstance().getGui();
			frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_CLOSING));
		}
	}
}
