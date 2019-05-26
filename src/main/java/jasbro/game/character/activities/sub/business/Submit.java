package jasbro.game.character.activities.sub.business;

import java.util.List;

import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.texts.TextUtil;

public class Submit extends Whore {
	
	@Override
	public int rateCustomer(Customer customer) {
		int rating = super.rateCustomer(customer);
		if (customer.getPreferredSextype() == Sextype.BONDAGE) {
			return rating * 4;
		}
		else {
			rating = rating / 8;
			if (rating == 0) {
				rating = 1;
			}
			return rating;
		}
	}
	
	@Override
	public MessageData getBaseMessage() {
		MessageData message = super.getBaseMessage();
		if (getSexType() == Sextype.BONDAGE) {
			message.addToMessage(TextUtil.t("submit.basic", getCharacter(), getMainCustomer()));
		}
		else {
			message.addToMessage(TextUtil.t("submit.displeased", getCharacter(), getMainCustomer()));
		}
		return message;
	}
	
	@Override
	public void perform() {
		if (getSexType() != Sextype.BONDAGE) {
			getMainCustomer().addToSatisfaction(-15, this);
		}
		else {
			getMainCustomer().changePayModifier(0.2f);
		}
		super.perform();
	}
	
	@Override
	public List<Sextype> getPossibleSextypes(Customer customer) {
		List<Sextype> sextypes = super.getPossibleSextypes(customer);
		if (!sextypes.contains(Sextype.BONDAGE)) {
			sextypes.add(Sextype.BONDAGE);
		}
		return sextypes;
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = super.getStatModifications();
		if (getSexType() == Sextype.BONDAGE) {
			modifications.add(new ModificationData(TargetType.ALL, -5, EssentialAttributes.HEALTH));
			if(!(getCharacter().getTraits().contains(Trait.LEGACYWHORE))){
				modifications.add(new ModificationData(TargetType.TRAINER, -0.1f, BaseAttributeTypes.COMMAND));
			}	
		}
		return modifications;
	}
}