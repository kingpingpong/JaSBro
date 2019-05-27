package jasbro.game.housing;

import jasbro.texts.TextUtil;

public enum RoomSlotType {
	EMPTYSPACE(0, 0), SMALLROOM(1, 500), LARGEROOM(3, 1000), OUTDOOR(8, 1500), UNDERGROUND(5, 2000);

	private int conCost;
	private int downTime;
	
	private RoomSlotType(int downTime) {
		this(downTime, 0);
	}

	RoomSlotType(int downTime, int cost) {
		this.downTime = downTime;
		this.conCost = cost;
	}

	public String getText() {
		return TextUtil.t(this.toString());
	}

	public int getConCost() {
		return conCost;
	}

	public int getDownTime() {
		return downTime;
	}
	
}