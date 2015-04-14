package jasbro.game.events.rooms;

import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public abstract class AbstractRoomEventHandler implements RoomEventHandler {

	private EventType handledType;
	
	public void setHandledType(final EventType handledType) {
		this.handledType = handledType;
	}
	
	@Override
	public void handleEvent(MyEvent event) {
		if(event.getType() == handledType) {
			handleEventInternal(event);
		}
	}
	
	protected abstract void handleEventInternal(final MyEvent event);
}
