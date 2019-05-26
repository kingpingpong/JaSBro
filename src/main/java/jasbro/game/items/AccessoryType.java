package jasbro.game.items;

import jasbro.texts.TextUtil;

public enum AccessoryType {
	ONEHANDED, TWOHANDED, NECKLACE, GLOVES;
	
	public String getText() {
		return TextUtil.t(this.toString());
	}
	
	public int getHandsUsed() {
		switch(this) {
		case ONEHANDED:
			return 1;
		case TWOHANDED:
			return 2;
		default:
			return 0;
		}
	}
}