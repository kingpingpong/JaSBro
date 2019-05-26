package jasbro.game.events;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Attribute;

public class AttributeChangedEvent extends MyEvent {
	private float amount;
	private RunningActivity activity;
	
	public AttributeChangedEvent(EventType eventType, Attribute attribute, float amount, RunningActivity activity) {
		super(eventType, attribute);
		this.amount = amount;
		this.activity = activity;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public RunningActivity getActivity() {
		return activity;
	}
	
	public Attribute getAttribute() {
		return (Attribute) getSource();
	}
}