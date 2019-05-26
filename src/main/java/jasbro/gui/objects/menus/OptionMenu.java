package jasbro.gui.objects.menus;

import jasbro.Jasbro;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class OptionMenu extends JMenu {
	public OptionMenu () {
		super (TextUtil.t("ui.options"));
		
		// Options button
		JMenuItem optionsButton = new JMenuItem (new PreferenceAction ());
		add (optionsButton);
		
		// About button
		//JMenuItem aboutButton = new JMenuItem (new AboutAction ());
		//add (aboutButton);
		
		// Help button
		//JMenuItem helpButton = new JMenuItem (new HelpAction ());
		//add (helpButton);
	}
	
	/*
	private class AboutAction extends AbstractAction {
		public AboutAction () {
			super (TextUtil.t("ui.about"));
		}
		
		public void actionPerformed (ActionEvent e) {
<<<<<<< .mine
			JOptionPane.showMessageDialog (RPGApp.getInstance().getGui(), "Version 0.1.29\nMade by Teferus\nContributors: Gam0rF4ce\n", "About", JOptionPane.DEFAULT_OPTION);
=======
			JOptionPane.showMessageDialog (RPGApp.getInstance().getGui(), "Version 0.1.32\nMade by Teferus\nContributors: Gam0rF4ce", "About", JOptionPane.DEFAULT_OPTION);
>>>>>>> .r35
		}
	}
	
	private class HelpAction extends AbstractAction {
		public HelpAction () {
			super (TextUtil.t("ui.help"));
		}
		
		public void actionPerformed (ActionEvent e) {
			JOptionPane.showMessageDialog (RPGApp.getInstance().getGui(), "Helptext", "Help", JOptionPane.DEFAULT_OPTION);
			// TODO improve help dialog
		}
	}*/
	
	private class PreferenceAction extends AbstractAction {
		
		private PreferencePanel preferencePanel;
		
		public PreferenceAction () {
			super (TextUtil.t("ui.preferences"));
			preferencePanel = new PreferencePanel();
		}
		
		public void actionPerformed (ActionEvent e) {
			if (JOptionPane.showConfirmDialog (Jasbro.getInstance().getGui(), preferencePanel, "Preferences", JOptionPane.OK_CANCEL_OPTION) == 
					JOptionPane.OK_OPTION) {
				try {
					preferencePanel.applyChanges();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog (Jasbro.getInstance().getGui(), TextUtil.t("ui.error.optionsSaveFailed"), "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	
	
}