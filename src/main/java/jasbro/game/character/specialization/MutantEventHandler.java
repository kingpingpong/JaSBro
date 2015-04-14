package jasbro.game.character.specialization;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.interfaces.MyCharacterEventListener;
import jasbro.texts.TextUtil;

public class MutantEventHandler implements MyCharacterEventListener {

    @Override
    public void handleEvent(MyEvent e, Charakter character) {
        if (e.getType() == EventType.ACTIVITYPERFORMED) {
            RunningActivity activity = (RunningActivity) e.getSource();
            if ((activity.getType() == ActivityType.MONSTERFIGHT && activity.getMainCustomer() != null)
                    || activity.getSextype() != null) {
                if (activity.getSextype() == Sextype.MONSTER || activity.getType() == ActivityType.MONSTERFIGHT) {
                    float mod = 1.5f;
                    if (activity.getSextype() == Sextype.MONSTER && activity.getType() == ActivityType.MONSTERFIGHT) {
                        mod = 1.8f;
                    }                    
                    activity.getAttributeModifications().add(
                            new AttributeModification(mod, SpecializationAttribute.GENETICADAPTABILITY, character));
                    activity.getMessages().get(0).addToMessage(
                            TextUtil.t("mutant.gain.monster", character));
                }
                else if (activity instanceof Whore) {
                    float mod = 0;
                    for (Customer customer : activity.getMainCustomers()) {
                        if (customer instanceof CustomerGroup) {
                            for (@SuppressWarnings("unused") Customer subCustomer : ((CustomerGroup) customer).getCustomers()) {
                                if (Util.getInt(0, 100) < 5) {
                                    mod += 0.3f;
                                }
                            }
                        }
                        else {
                            if (Util.getInt(0, 100) < 5) {
                                mod += 0.3f;
                            }
                        }
                    }
                    if (mod > 0) {
                        activity.getAttributeModifications().add(
                                new AttributeModification(mod, SpecializationAttribute.GENETICADAPTABILITY, character));
                        activity.getMessages().get(0).addToMessage(
                                TextUtil.t("mutant.gain.customers", character));
                    }
                }
                else {
                    for (Charakter otherCharacter : activity.getCharacters()) {
                        if (otherCharacter != character) {
                            if (character.getSpecializations().contains(SpecializationType.MUTANT)) {
                                int diff = otherCharacter.getFinalValue(SpecializationAttribute.GENETICADAPTABILITY) - 
                                        ((int)character.getAttribute(SpecializationAttribute.GENETICADAPTABILITY).getInternValue());
                                float modifyBy = 0.01f + diff / 25.0f;
                                activity.getAttributeModifications().add(
                                        new AttributeModification(modifyBy, SpecializationAttribute.GENETICADAPTABILITY, character));
                                if (modifyBy > 0.5f) {
                                    activity.getMessages().get(0).addToMessage(
                                            TextUtil.t("mutant.gain.otherCharacter", character, otherCharacter));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
