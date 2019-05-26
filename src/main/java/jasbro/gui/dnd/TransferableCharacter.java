package jasbro.gui.dnd;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TransferableCharacter implements Transferable {
	private int id;
	public final static DataFlavor FLAVOR = new DataFlavor(Integer.class, "Index of the Character");
	
	public TransferableCharacter(Charakter character) {
		this.id = Jasbro.getInstance().getData().getCharacters().indexOf(character);
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