package jasbro.gui.objects.menus.actions;

import jasbro.Jasbro;

import java.awt.event.ActionEvent;
import java.io.File;

public class SlotLoadAction extends SlotSaveAction {
	public SlotLoadAction (int slot) {
		super (slot);
	}
	
	public SlotLoadAction (String name, int slot) {
		super (name, slot);
	}

	public void actionPerformed (ActionEvent e) {
		if (getSlot() == -1) {
			Jasbro.getInstance().load(new File("quicksave.xml"));
		}
		else {
			Jasbro.getInstance().load(new File("save" + getSlot() + ".xml"));
		}
	}
}