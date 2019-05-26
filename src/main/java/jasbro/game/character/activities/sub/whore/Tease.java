package jasbro.game.character.activities.sub.whore;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Tease extends Whore {
	
	@Override
	public int rateCustomer(Customer customer) {
		int rating = super.rateCustomer(customer);
		if (customer.getPreferredSextype() == Sextype.FOREPLAY) {
			return rating;
		}
		else {
			rating = rating / 10;
			if (rating == 0) {
				rating = 1;
			}
			return rating;
		}
	}
	
	@Override
	public String checkPossible(RunningActivity activity, Charakter whore) {
		return "";
	}
	
	@Override
	public void init() {
		super.init();
		if (getSexType() != Sextype.FOREPLAY) {
			setSexType(Sextype.FOREPLAY);
			getMainCustomer().addToSatisfaction(-40, this);
		}
		getMainCustomer().addToSatisfaction(getCharacter().getFinalValue(SpecializationAttribute.STRIP) / 4, this);
	}
	
	@Override
	public MessageData getBaseMessage() {
		MessageData message = super.getBaseMessage();
		if (getMainCustomer().getPreferredSextype() != Sextype.FOREPLAY) {
			message.addToMessage(TextUtil.t("tease.basic2", getCharacter(), getMainCustomer()));
		}
		else {
			message.addToMessage(TextUtil.t("tease.basic", getCharacter(), getMainCustomer()));
		}
		List<ImageTag> tags = getCharacter().getBaseTags();
		if (Util.getRnd().nextBoolean()) {
			tags.add(0, ImageTag.MASTURBATION);
		}
		else {
			tags.add(0, ImageTag.DANCE);
		}		
		ImageData imageData = ImageUtil.getInstance().getImageDataByTags(tags, getCharacter().getImages());
		message.setImage(imageData);
		return message;
	}
	
	@Override
	public List<Sextype> getPossibleSextypes(Customer customer) {
		List<Sextype> sextypes = new ArrayList<Sextype>();
		sextypes.add(Sextype.FOREPLAY);
		return sextypes;
	}
	
	@Override
	public void perform() {
		getMainCustomer().changePayModifier(-0.33f);
		super.perform();
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modificationData  = new ArrayList<ModificationData>();
		modificationData.add(new ModificationData(TargetType.ALL, 1f, Sextype.FOREPLAY));
		modificationData.add(new ModificationData(TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
		modificationData.add(new ModificationData(TargetType.TRAINER, -0.1f, BaseAttributeTypes.OBEDIENCE));
		modificationData.add(new ModificationData(TargetType.ALL, 0.4f, SpecializationAttribute.SEDUCTION));
		modificationData.add(new ModificationData(TargetType.ALL, 0.2f, SpecializationAttribute.STRIP));
		modificationData.add(new ModificationData(TargetType.ALL, -4, EssentialAttributes.ENERGY));
		return modificationData;
	}
	
	@Override
	public Float getAmountActions() {
		return 0.7f;
	}
	@Override
	public float getExecutionModifier() {
		return -0.5f;
	}
	@Override
	public float getCooldownModifier() {
		return -100.0f;
	}
}