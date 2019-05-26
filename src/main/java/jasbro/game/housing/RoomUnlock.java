package jasbro.game.housing;

import jasbro.game.interfaces.UnlockObject;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

public class RoomUnlock implements UnlockObject {
	
	private final String roomInfoId;
	private boolean locked = false;
	
	public RoomUnlock(final String roomInfoId) {
		this.roomInfoId = roomInfoId;
	}
	
	public String getRoomInfoId() {
		return this.roomInfoId;
	}

	@Override
	public String getText() {
		return RoomInfoUtil.getRoomInfo(roomInfoId).getText();
	}

	@Override
	public String getDescription() {
		return RoomInfoUtil.getRoomInfo(roomInfoId).getDescription();
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

	@Override
	public ImageData getImage() {
		return RoomInfoUtil.getRoomInfo(roomInfoId).getImage();
	}

	@Override
	public void setLocked(final boolean locked) {
		this.locked = locked;
	}

}
