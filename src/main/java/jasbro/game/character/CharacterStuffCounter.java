package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Nurse;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.conditions.SleepDeprivation;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerType;
import jasbro.game.world.Time;
import jasbro.gui.GuiUtil;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;

import java.util.HashMap;
import java.util.Map;

public class CharacterStuffCounter {
	private Map<String, Long> counterMap;
	@SuppressWarnings("unused")
	private transient Map<String, Integer> map;
	
	private Map<String, Long> getCounterMap() {
		if (counterMap == null) {
			counterMap = new HashMap<String, Long>();
		}
		return counterMap;
	}
	
	public Long get(String key) {
		if (!getCounterMap().containsKey(key)) {
			return 0l;
		} else {
			return getCounterMap().get(key);
		}
	}
	
	public Long get(CounterNames key) {
		return get(key.toString());
	}
	
	public void add(CounterNames key) {
		add(key.toString(), 1l);
	}
	
	public void add(String key) {
		add(key, 1l);
	}
	
	public void add(CounterNames key, Long value) {
		add(key.toString(), value);
	}
	
	public void add(String key, Long value) {
		if (!getCounterMap().containsKey(key)) {
			getCounterMap().put(key, value);
		} else {
			getCounterMap().put(key, getCounterMap().get(key) + value);
		}
	}
	
	public void reset(String key) {
		if (getCounterMap().containsKey(key)) {
			counterMap.remove(key);
		}
	}
	
	public void handleEvent(MyEvent e, Charakter character) {
		if (e.getType() == EventType.ACTIVITYPERFORMED) {
			RunningActivity activity = (RunningActivity) e.getSource();
			String activityString = activity.getType().toString();
			add(activityString);
			
			if (activity.getType() == ActivityType.BREAK && get(activityString) % 10 == 0) {
				if (character.getType() == CharacterType.SLAVE) {
					if (!character.getTraits().contains(Trait.OBEDIENT)) {
						character.addTrait(Trait.OBEDIENT);
					} else {
						character.getAttribute(BaseAttributeTypes.OBEDIENCE).addToValue(2);
					}
					character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(-2);
					GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.break", character), ImageTag.HURT, character, true), e);
				}
			}
			else if (activity.getType() == ActivityType.SWIM && !character.getTraits().contains(Trait.FIT) && get(activityString) % 30 == 0) {
				character.addTrait(Trait.FIT);
				GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.swim", character), ImageTag.SWIM, character, true), e);
			}
			else if (activity.getType() == ActivityType.ADVERTISE && character.getTraits().contains(Trait.SHY) && get(activityString) % 50 == 0) {
				character.removeTrait(Trait.SHY);
				GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.advertise", character), ImageTag.CLOTHED, character, true), e);
			}
			else if (activity.getType() == ActivityType.BARTEND || activity.getType() == ActivityType.COOK || 
					activity.getType() == ActivityType.CLEAN || activity.getType() == ActivityType.SELLFOOD) {
				if (get(activityString) % 300 == 0 && !character.getTraits().contains(Trait.HELPFUL)) {
					long cookAmount = get(ActivityType.COOK.toString()) + get(ActivityType.SELLFOOD.toString());
					if ((cookAmount > 50 && get(ActivityType.CLEAN.toString()) > 50) || 
							(cookAmount > 50 && get(ActivityType.BARTEND.toString()) > 50) || 
							(get(ActivityType.BARTEND.toString()) > 50 && get(ActivityType.CLEAN.toString()) > 50)) {
						character.addTrait(Trait.HELPFUL);
						GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.housework", character), ImageTag.MAID, character, true), e);
					}
				}
			}
			else if (activity.getType() == ActivityType.SLEEP || activity.getType() == ActivityType.CAMP) {
				reset(CounterNames.NOSLEEP.toString());
				add(CounterNames.NOSLEEP.toString(), -1l);
			}
			else if (activity.getType() == ActivityType.NURSE) {
				Nurse nurse = (Nurse) activity;
				if (nurse.getNurse() != character) {
					reset(CounterNames.NOSLEEP.toString());
					add(CounterNames.NOSLEEP.toString(), -1l);
				}
			}
			else if (activity.getType() == ActivityType.IDLE && Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
				reset(CounterNames.NOSLEEP.toString());
				add(CounterNames.NOSLEEP.toString(), -1l);
			}
			
			if(activity.getType()==ActivityType.WHORE){
				if(activity.getMainCustomer().getType()==CustomerType.GROUP){
					CustomerGroup group = (CustomerGroup) activity.getMainCustomer();
					add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) group.getCustomers().size());
				}
				else
					add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) activity.getMainCustomers().size());
			}
			//add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) activity.getMainCustomers().size());
			add(CounterNames.MAINCUSTOMERS.toString(), (long) activity.getMainCustomers().size());
			add(CounterNames.SECONDARYCUSTOMERS.toString(), (long) activity.getCustomers().size());
			
			
			
			
			
		}
		else if (e.getType() == EventType.ACTIVITYFINISHED) {
			RunningActivity activity = (RunningActivity) e.getSource();
			if (activity.getCharacters().size() > 0) {
				add(CounterNames.MONEYEARNED.toString(), (long)(activity.getIncome() / activity.getCharacters().size()));
			}
		}
		else if (e.getType() == EventType.NEXTDAY) {
			reset(CounterNames.CUSTOMERSSERVEDTODAY.toString());
			add(CounterNames.DAYS.toString());
			if (get(CounterNames.DAYS.toString()) % 1000 == 0 && character.getType() == CharacterType.TRAINER &&
					character != Jasbro.getInstance().getData().getProtagonist() && !character.getTraits().contains(Trait.LOYAL)) {
				character.addTrait(Trait.LOYAL);
				GuiUtil.addMessageToEvent(new MessageData(TextUtil.t("counterevents.loyal", character), ImageTag.CLOTHED, character, true), e);
			}
		}
		else if (e.getType() == EventType.NEXTSHIFT) {
			add(CounterNames.NOSLEEP.toString(), 1l);
			if (get(CounterNames.NOSLEEP.toString()) >= 9) {
				character.addCondition(new SleepDeprivation());
			}
		}

	}
	
	public static enum CounterNames {
		DAYS, SICK, MONEYEARNED, NOSLEEP, CUSTOMERSSERVEDTODAY, MAINCUSTOMERS, SECONDARYCUSTOMERS, CHILDREN
		
	}
	
	
}