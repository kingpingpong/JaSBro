package jasbro.game.events;

public class MoneyChangedEvent extends MyEvent {
	private long amount;
	
	public MoneyChangedEvent(EventType type, Object source, long amount) {
		super(type, source);
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}
}
