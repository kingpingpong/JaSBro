package jasbro.game.events.rooms;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;

public class SexSatisfactionEventHandler extends AbstractRoomEventHandler {
	
	private final Sextype bonusType;
	private final int bonusAmount;
	
	public SexSatisfactionEventHandler(final EventType handledType, final Sextype bonusType, final int bonusAmount) {
		this.bonusType = bonusType;
		this.bonusAmount = bonusAmount;
		setHandledType(handledType);
	}
	
	@Override
	protected void handleEventInternal(MyEvent event) {
		RunningActivity a = (RunningActivity) event.getSource();
		for(Customer c : a.getMainCustomers()) {
			if(c.getPreferredSextype() == bonusType) {
				c.addToSatisfaction(bonusAmount, this);
			}
		}
	}
	
}