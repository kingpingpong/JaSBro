package jasbro.util;

import jasbro.gui.pictures.ImageTag;

import javax.swing.JCheckBox;

public class LinkedJCheckBox extends JCheckBox {
	
	private final ImageTag itLinkedTag;
	
	public LinkedJCheckBox (ImageTag tag) {
		super(tag.name().substring(0, 1) + tag.name().substring(1).toLowerCase());
		itLinkedTag = tag;
	}
	
	public ImageTag getLinkedTag() {
		return itLinkedTag;
	}
}
