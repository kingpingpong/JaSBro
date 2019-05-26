package jasbro.game.character.traits.perktrees;

import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Talk;
import jasbro.game.character.activities.sub.Train;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;

import java.util.Map;

public class TrainerPerks {
	
	public static class EffectiveTrainer extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYPERFORMED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Train) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (!(attributeModification.getAttributeType() instanceof EssentialAttributes)) {
							attributeModification.addModificator(attributeModification.getBaseAmount() * 0.3f);
						}
					}
				}
			}
		}
	}

	public static class Motivator extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYFINISHED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Talk) {
					for (Charakter curCharacter : activity.getCharacters()) {
						for (Condition condition : curCharacter.getConditions()) {
							if (condition instanceof Buff) {
								Buff buff = (Buff) condition;
								for(Map.Entry<AttributeType, Integer> entry : buff.getAttributeModifiers().entrySet()) {
									if(entry.getKey() instanceof BaseAttributeTypes) {
										entry.setValue(entry.getValue() + 1);
									} else {
										entry.setValue(entry.getValue() + 10);
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
