package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Bartend extends RunningActivity implements BusinessSecondaryActivity {
    private final static int COSTDRINK = 6;
    private final static int PROFITDRINK = 4;
    private int bonus;

    private MessageData messageData;
    private List<Charakter> bartenders = new ArrayList<Charakter>();

    @Override
    public void init() {
        bartenders.addAll(getCharacters());
    }

    @Override
    public void perform() {

        int amountEarned = 0;
        Charakter bartender;
        for (Customer customer : getCustomers()) {
            bartender = bartenders.get(Util.getInt(0, bartenders.size()));
            customer.addToSatisfaction(5 + bartender.getFinalValue(BaseAttributeTypes.CHARISMA) / 8 + 
                    bartender.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 10, this);
            int amountDrinks = Util.getInt(1, 4);
            if (customer.getType() == CustomerType.BUM) {
                amountDrinks++;
            }
            amountEarned += PROFITDRINK * amountDrinks;
            int tips = (customer.getMoney() / 200) + Util.getInt(1, 4);

            customer.payFixed(COSTDRINK * amountDrinks);
            tips = customer.pay(tips, bartender.getMoneyModifier());

            amountEarned += tips;
            customer.changePayModifier(0.1f);
        }
        modifyIncome(amountEarned);

        String arguments[] = { getCustomers().size() + "", "" + getIncome() };
        if (bartenders.size() < 2) {
            messageData.addToMessage("\n\n" + TextUtil.t("bartend.result", getCharacter(), arguments));
        } else {
            messageData.addToMessage("\n\n" + TextUtil.t("bartend.result.group", arguments));
        }
    }

    @Override
    public MessageData getBaseMessage() {
        Object arguments[] = { TextUtil.listCharacters(bartenders) };
        String messageText = TextUtil.t("bartend.basic", arguments);
        this.messageData = new MessageData(messageText, ImageUtil.getInstance()
                .getImageDataByTag(ImageTag.BARTEND, getCharacter()), getBackground());
        if (bartenders.size() > 1) {
            messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartenders.get(1)));
        }
        return this.messageData;
    }

    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 1.1f, SpecializationAttribute.BARTENDING));
        modifications.add(new ModificationData(TargetType.TRAINER, -0.15f, BaseAttributeTypes.COMMAND));
        return modifications;
    }

    @Override
    public int getAppeal() {
        return Util.getInt(2, 8);
    }

    @Override
    public int getMaxAttendees() {
        int amount = 0;
        for (Charakter bartender : getCharacters()) {
            amount += 5 + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 5;
        }
        return amount+bonus;
    }

    public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
}
