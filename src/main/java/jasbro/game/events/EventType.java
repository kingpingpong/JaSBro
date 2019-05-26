
package jasbro.game.events;


/**
 * Types of events that can be triggered and handled in Jasbro.
 * @author Azrael
 */
public enum EventType {
	/**
	 * Event triggered after initialization of activity, but before it is actually performed.
	 * The event source is the RunningActivity.
	 */
	ACTIVITY(true), 
	
	/**
	 * Event triggered immediately after activity has been performed, before modifications are applied and message screen is created.
	 * The event source is the RunningActivity.
	 */
	ACTIVITYPERFORMED(true), 
	
	/**
	 * Event triggered after stat and fame modification have been applied, and after activity message screen has been created.
	 * The event source is the RunningActivity.
	 */
	ACTIVITYFINISHED(true), 
	
	/**
	 * Event triggered after activity instantiation, before initialization. 
	 * The event source is the RunningActivity.
	 */
	ACTIVITYCREATED(true),
	
	/**
	 * Event triggered when a character has been removed from the GameData.
	 * The event source is the Charakter.
	 */
	CHARACTERLOST,
	
	/**
	 * Event triggered when a trainer is hired or a slave is bought. Not triggered by temporary slaves.
	 */
	CHARACTERGAINED, 
	
	/**
	 * Triggered when a slave has been sold.
	 * The event source is the Auction.
	 */
	SLAVESOLD,
	
	/**
	 * Triggered when any character attribute is being changed. 
	 * The event source is the AttributeModification.
	 */
	ATTRIBUTECHANGE, 
	
	/**
	 * Triggered immediately after character attribute change has been applied.
	 * The event is an instance of AttributeChangedEvent.
	 * The source is the Attribute.
	 */
	ATTRIBUTECHANGED,
	
	/**
	 * Triggered whenever changes to a planned activity are made.
	 * The source is the PlannedActivity.
	 */
	ACTIVITYCHANGE,
	
	/**
	 * Triggered when a character's energy reaches zero.
	 * The event is an instance of AttributeChangedEvent.
	 * The source is the (energy) Attribute.
	 */
	ENERGYZERO, 
	
	/**
	 * Triggered when a character's health reaches zero.
	 * The event is an instance of AttributeChangedEvent.
	 * The source is the (health) Attribute.
	 */
	HEALTHZERO(true), 
	
	/**
	 * Triggered by HEALTHZERO event unless cancelled. Immediately before the coffin message is shown.
	 * The event source is the Charakter.
	 */
	CHARACTERDEATH,
	
	/**
	 * Shift phase event which is triggered after shift has been performed.
	 * The event source is null.
	 */
	NEXTSHIFT(true), 
	
	/**
	 * Shift phase event which is triggered after last shift (night shift) of the day has been performed.
	 * The event source is null.
	 */
	NEXTDAY(true),
	
	/**
	 * A bit unfortunate that this is needed now, but I see no way around it
	 * characters need to check if their activities are still valid at the very beginning of a new shift
	 * previously each call of getActivity in plannedactivity would trigger that check
	 * but due to the increased complexity through custom activities that is no longer viable
	 * should not be used for anything else 
	 */
	NEXTSHIFTSTARTED,
	
	/**
	 * Triggered whenever an item is equipped/unequipped.
	 * The event source is the Equipment.
	 */
	ITEMUSED,
	
	/**
	 * Shift phase event which is triggered when the shift has been started.
	 * The event source is null.
	 */
	SHIFTSTART(true), 
	
	/**
	 * Shift phase event which is triggered after house customers have been determined.
	 * The event source is the House.
	 */
	CUSTOMERSARRIVE,
	
	/**
	 * Triggered whenever money is earned (whether through activities, sales, cheats.
	 * Source type varies.
	 */
	MONEYEARNED, 
	
	/**
	 * Triggered whenever money is spent (whether through activities, purchases...
	 * Source type varies.
	 */
	MONEYSPENT, 
	
	/**
	 * Triggered when an attempt is made to spend money that isn't there.
	 * The event source is null.
	 */
	BROKE,
	
	/**
	 * Battle event which is triggered at the beginning of each battle round.
	 * The event source is the Battle.
	 */
	ATTACK, 
	
	/**
	 * Battle event which is triggered whenever an attack misses.
	 * The event source is the Battle.
	 */
	ATTACKMISS, 
	
	/**
	 * Battle event which is triggered whenever an attack is blocked.
	 * The event source is the Battle.
	 */
	ATTACKBLOCK, 
	
	/**
	 * Battle event which is triggered whenever an attack does critical damage.
	 * The event source is the Battle.
	 */
	ATTACKCRIT, 
	
	/**
	 * Battle event which is triggered whenever an attack hits its target.
	 * The event source is the Battle.
	 */
	ATTACKHIT,
	
	/**
	 * Triggered for any state change to a character.
	 * The event source is the Charakter.
	 */
	STATUSCHANGE,

	MOTIVATIONLOW(true),
	MOTIVATIONHIGH(true),
	MOTIVATIONNORMAL(true),
	
	/**
	 * Triggered once at the beginning of the game, no source
	 */
	GAMESTART(true);
	
	private boolean customContentRelevant = false; //Current simple way to prevent double usage problems due to small events during custom events
	
	private EventType() {
	}
	
	private EventType(boolean customContentRelevant) {
		this.customContentRelevant = customContentRelevant;
	}
	
	public boolean isCustomContentRelevant() {
		return customContentRelevant;
	}
	
	
	
	
}