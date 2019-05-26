package jasbro.gui.objects.menus;

import jasbro.gui.objects.menus.actions.SlotSaveAction;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class SaveMenu extends JMenu {
	public SaveMenu () {
		super (TextUtil.t("ui.save"));
		
		// Quicksave button
		JMenuItem saveQuickButton = new JMenuItem (new SlotSaveAction (TextUtil.t("ui.quicksave"), -1));
		saveQuickButton.setMnemonic (KeyEvent.VK_Q);
		add (saveQuickButton);
		
		// Autosave button
		/*JMenuItem saveAutoButton = new JMenuItem (new SlotSaveAction (TextUtil.t("ui.autosave"), -2));
		saveAutoButton.setMnemonic (KeyEvent.VK_A);
		saveAutoButton.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		add (saveAutoButton);*/
		
		JMenuItem saveSlotButton;
		
		// Slot buttons
		for (short i = 1; i <= ConfigHandler.getSetting (Settings.SAVESLOTS, 3); i++) {
			saveSlotButton = new JMenuItem (new SlotSaveAction (i));
			if (i < 10) {
				saveSlotButton.setMnemonic (48 + i);
				saveSlotButton.setAccelerator (KeyStroke.getKeyStroke (48 + i, ActionEvent.CTRL_MASK));
			}
			add (saveSlotButton);
		}
	}
}