package jasbro.gui.objects.menus.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jasbro.Jasbro;

public class SlotLoadAction extends SlotSaveAction {
	private static final Logger LOGGER = LogManager.getLogger(SlotLoadAction.class);
	
	public SlotLoadAction(int slot) {
		super(slot);
	}

	public SlotLoadAction(String name, int slot) {
		super(name, slot);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (getSlot() == -1) {
				Jasbro.getInstance().load(new File("quicksave.xml"));
			} else {
				Jasbro.getInstance().load(new File("save" + getSlot() + ".xml"));
			}
		} catch (IOException ex) {
			LOGGER.error("Failed to load save", ex);
		}
	}
}