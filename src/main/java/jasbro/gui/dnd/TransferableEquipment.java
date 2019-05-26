package jasbro.gui.dnd;

import jasbro.game.items.Equipment;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TransferableEquipment implements Transferable {
	private String id;
	public final static DataFlavor FLAVOR = new DataFlavor(String.class, "Item id");
	
	public TransferableEquipment(Equipment equipment) {
		this.id = equipment.getId();
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = { FLAVOR };
		return flavors;
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return FLAVOR == flavor;
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return id;
	}
}