package jasbro.game.character.traits.perktrees;

import jasbro.game.character.Charakter;
import jasbro.Jasbro;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Clean;
import jasbro.game.character.activities.sub.Cook;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class LegacyPerks {

	public static class Genius extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.BARTEND) {
					character.addCondition(new Buff.HornyBuff(character));	
				}
			}
		}	
	}
	
	public static class BenefactorStreets extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				Jasbro.getInstance().getData().spendMoney(1000, this);
			}
		}
	}
	public static class BenefactorShops extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				Jasbro.getInstance().getData().spendMoney(5000, this);
			}
		}
	}
	public static class BenefactorSlavemarket extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				Jasbro.getInstance().getData().spendMoney(10000, this);
			}
		}
	}
	public static class BenefactorCarpenters extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				Jasbro.getInstance().getData().spendMoney(12000, this);
			}
		}
	}
	public static class BenefactorKingdom extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				Jasbro.getInstance().getData().spendMoney(50000, this);
			}
		}
	}
	
	
	
	public static class LegacyNone extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				for (AttributeModification attributeModification : activity.getAttributeModifications()) {
					if (attributeModification.getAttributeType() == BaseAttributeTypes.COMMAND) {
						float amount = attributeModification.getBaseAmount();
						if (amount < 0) {
							float change = Math.abs(amount)*0.20f;
							attributeModification.addModificator(change);
						}
					}
				} 
			} 
		} 
	}
}
