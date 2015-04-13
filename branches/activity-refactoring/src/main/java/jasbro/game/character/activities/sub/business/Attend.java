package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Attend extends RunningActivity implements BusinessSecondaryActivity {

    private MessageData messageData;
    private List<Charakter> dancers = new ArrayList<Charakter>();
    private List<Charakter> bartenders = new ArrayList<Charakter>();

    @Override
    public void init() {
        for (Charakter character : getCharacters()) {
            if (character.getSpecializations().contains(SpecializationType.DANCER) && dancers.size() < 2) {
                dancers.add(character);
            } else {
                bartenders.add(character);
            }
        }

    }

    @Override
    public void perform() {
        int skill = 0;
        for (Charakter dancer : dancers) {
            skill += dancer.getCharisma() + dancer.getFinalValue(SpecializationAttribute.STRIP) / 4;
        }
        for (Charakter bartender : bartenders) {
            skill += bartender.getCharisma() / 2 + bartender.getIntelligence() / 2 + 
                    bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 4;
        }
        skill /= 3;

        int amountEarned = 0;
        for (Customer customer : getCustomers()) {
            customer.addToSatisfaction(skill, this);
            int tips = (int) (customer.getMoney() / (2000.0 / skill) + Util.getInt(10, 20));
            tips = customer.pay(tips, getCharacter().getMoneyModifier());
            amountEarned += tips;
        }
        modifyIncome(amountEarned);

        Object arguments[] = { TextUtil.listCharacters(dancers), TextUtil.listCharacters(bartenders), getCustomers().size(), amountEarned };

        messageData.addToMessage(TextUtil.t("cabaret.result", arguments));

    }

    @Override
    public MessageData getBaseMessage() {
        String messageText = TextUtil.t("cabaret.basic", getCharacters());
        messageText += "\n\n";
        this.messageData = new MessageData(messageText, null, getBackground());
        for (Charakter character : getCharacters()) {
            if (dancers.contains(character)) {
                this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character));
            }
            else {
                this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, character));
            }
        }
        return this.messageData;
    }

    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        for (Charakter dancer : dancers) {
            modifications.add(new ModificationData(TargetType.SINGLE, dancer, -30, EssentialAttributes.ENERGY));
            modifications.add(new ModificationData(TargetType.SINGLE, dancer, 1.0f, SpecializationAttribute.STRIP));
            modifications.add(new ModificationData(TargetType.SINGLE, dancer, 0.02f, BaseAttributeTypes.STAMINA));
            modifications.add(new ModificationData(TargetType.SINGLE, dancer, 0.02f, BaseAttributeTypes.CHARISMA));
        }
        for (Charakter bartender : bartenders) {
            modifications.add(new ModificationData(TargetType.SINGLE, bartender, -15, EssentialAttributes.ENERGY));
            modifications.add(new ModificationData(TargetType.SINGLE, bartender, 1.0f, SpecializationAttribute.BARTENDING));
            modifications.add(new ModificationData(TargetType.SINGLE, bartender, 0.02f, BaseAttributeTypes.INTELLIGENCE));
            modifications.add(new ModificationData(TargetType.SINGLE, bartender, 0.02f, BaseAttributeTypes.CHARISMA));
        }
        modifications.add(new ModificationData(TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
        return modifications;
    }

    @Override
    public int getAppeal() {
        int appeal = 0;
        for (Charakter dancer : dancers) {
            appeal += (dancer.getCharisma() + dancer.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
        }
        for (Charakter bartender : bartenders) {
            appeal += (bartender.getCharisma() + bartender.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
        }
        return appeal;
    }

    @Override
    public int getMaxAttendees() {
        return 20;
    }

}
