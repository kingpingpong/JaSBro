package jasbro.game.character.specialization;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyCharacterEventListener;

public class MutantEventHandler implements MyCharacterEventListener {
	
	@Override
	public void handleEvent(MyEvent e, Charakter character) {
		if (e.getType() == EventType.ACTIVITYPERFORMED) {
			RunningActivity activity = (RunningActivity) e.getSource();
			switch(activity.getType()){
			case STRIP:
				if(character.getTraits().contains(Trait.MORPHFELINE) || character.getTraits().contains(Trait.FELINE))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case BARTEND:
				if(character.getTraits().contains(Trait.MORPHLAGOMORPH) || character.getTraits().contains(Trait.LAGOMORPH))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case WHORE:
				if(character.getTraits().contains(Trait.MORPHVULPINE) || character.getTraits().contains(Trait.VULPINE))
					activity.getAttributeModifications().add(new AttributeModification(0.15f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case CLEAN:
				if(character.getTraits().contains(Trait.MORPHINSECT) || character.getTraits().contains(Trait.INSECT))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case COOK:
				if(character.getTraits().contains(Trait.MORPHINSECT) || character.getTraits().contains(Trait.INSECT))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case SELLFOOD:
				if(character.getTraits().contains(Trait.MORPHINSECT) || character.getTraits().contains(Trait.INSECT))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case NURSE:
				if(character.getTraits().contains(Trait.MORPHAQUATIC) || character.getTraits().contains(Trait.AQUATIC))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case ADVERTISE:
				if(character.getTraits().contains(Trait.MORPHAVIAN) || character.getTraits().contains(Trait.AVIAN))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case FIGHT:
				if(character.getTraits().contains(Trait.MORPHREPTILIAN) || character.getTraits().contains(Trait.REPTILIAN))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			case MONSTERFIGHT:
				if(character.getTraits().contains(Trait.MORPHREPTILIAN) || character.getTraits().contains(Trait.REPTILIAN))
					activity.getAttributeModifications().add(new AttributeModification(0.30f, SpecializationAttribute.TRANSFORMATION, character));
				break;
			}
		}
	}
	
}