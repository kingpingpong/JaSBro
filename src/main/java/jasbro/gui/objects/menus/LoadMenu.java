package jasbro.gui.objects.menus;

import jasbro.gui.objects.menus.actions.SlotLoadAction;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class LoadMenu extends JMenu {
	public LoadMenu () {
		super (TextUtil.t("ui.load"));
		
		// Quickload button
		JMenuItem loadQuickButton = new JMenuItem (new SlotLoadAction (TextUtil.t("ui.quickload"), -1));
		loadQuickButton.setMnemonic (KeyEvent.VK_Q);
		add (loadQuickButton);
		
		// Autosave load button
		/*JMenuItem loadAutoButton = new JMenuItem (new SlotLoadAction (TextUtil.t("ui.autosave"), -2));
		loadAutoButton.setMnemonic (KeyEvent.VK_A);
		loadAutoButton.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_A, ActionEvent.ALT_MASK));
		add (loadAutoButton);*/
		
		// Slot buttons
		File[] saveGames = getSaveGames();
		if (saveGames != null) {
			Pattern p = Pattern.compile ("(?<=save)\\d*(?=.xml)");
			for (File f : saveGames) {
				// Find the first continuous row of digits at the start of the file name
				Matcher m = p.matcher (f.getName());
				int i = 0;
				if (m.find()) {
					try {
						// Attempt to store the resulting number (possibly empty) in i
						i = Integer.parseInt (m.group());
					} catch (NumberFormatException e) {}
				}
				// Create the button if it worked and is not autosave (-2) or quicksave (-1) slot
				if (i > 0) {
					JMenuItem loadSlotButton; 
					
					loadSlotButton = new JMenuItem (new SlotLoadAction (i));
					add (loadSlotButton);
					if (i < 10) {
						loadSlotButton.setMnemonic (48 + i);
						loadSlotButton.setAccelerator (KeyStroke.getKeyStroke (48 + i, ActionEvent.ALT_MASK));
					}
				}
			}
		}
	}
	
	// Get a list of all saved games in the folder
	public File[] getSaveGames () {
		return new File (".").listFiles (new FilenameFilter() {
			public boolean accept(File directory, String name) {
				if (name.endsWith (".xml")) {
					return true;
				}
				return false;
			}
		});
	}
}