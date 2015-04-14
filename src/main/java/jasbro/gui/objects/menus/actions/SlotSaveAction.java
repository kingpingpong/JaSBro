package jasbro.gui.objects.menus.actions;

import jasbro.Jasbro;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

public class SlotSaveAction extends AbstractAction {
	
	private int slot;

	public SlotSaveAction (int slot) {
		super (TextUtil.t("ui.slot") + " " + slot); // TODO change ui.load & ui.save to ui.slot
		this.slot = slot;
	}
	
	public SlotSaveAction (String text, int slot) {
		super (text);
		this.slot = slot;
	}

	public void actionPerformed (ActionEvent e) {
		if (getSlot() == -1) {
			Jasbro.getInstance().save(new File("quicksave.xml"));
		}
		else {
			Jasbro.getInstance().save (new File("save" + slot + ".xml"));
		}
		Jasbro.getInstance().getGui().getMainMenuBar().rebuildLoadMenu();
	}

	public int getSlot() {
		return slot;
	}
	
	
}
