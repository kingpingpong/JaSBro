package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Study extends RunningActivity {

	private SpecializationType specialization;

	@Override
	public void init() {

		List<SelectionData<SpecializationType>> options = new ArrayList<SelectionData<SpecializationType>>();
		for (SpecializationType specialization : Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations()) {
			if (specialization != SpecializationType.TRAINER && specialization != SpecializationType.SLAVE &&
					specialization != SpecializationType.UNDERAGE && specialization.isTeachable()
					) {
				SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
				option.setSelectionObject(specialization);

				String type;
				if (specialization == SpecializationType.SEX || specialization == SpecializationType.KINKYSEX) {
					type = "sex";
				}
				else {
					type = "specialization";
				}
				if (isBasicTraining(specialization, getCharacter())) {
					Object arguments[] = { specialization.getText() };
					option.setButtonText(TextUtil.t("study.option."+ type +".free", getCharacter(), arguments));
				} else {
					int price = 1000;
					if(isHiddenTraining(specialization, getCharacter()) && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY))
						price=20000;
					else if (isAdvancedTraining(specialization, getCharacter())) {
						price = 10000;
					}
					if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTLIBRARY))
						price*=0.8;
					Object arguments[] = { specialization.getText(), price};
					option.setButtonText(TextUtil.t("study.option."+ type +".costs", getCharacter(), arguments));
					if (!Jasbro.getInstance().getData().canAfford(price)) {
						option.setEnabled(false);
					}
				}

				options.add(option);
			}
		}
		SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
		option.setButtonText(TextUtil.t("ui.cancel"));
		options.add(option);

		SelectionData<SpecializationType> selectedOption = new SelectionScreen<SpecializationType>()
				.select(options,
						ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()), null,
						getCharacter().getBackground(), TextUtil.t("study.option.description", getCharacter()));
		specialization = selectedOption.getSelectionObject();

		if (specialization != null) {
			int price=0;
			if(isHiddenTraining(specialization, getCharacter()) && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY))
				price=20000;
			else if(isAdvancedTraining(specialization, getCharacter()))
				price=10000;
			else if (!isBasicTraining(specialization, getCharacter()))
				price=1000;
			else
				price=0;
			if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTLIBRARY))
				price*=80/100;
			Jasbro.getInstance().getData().spendMoney(price, this);
		}
	}

	@Override
	public MessageData getBaseMessage() {
		if (specialization != null) {
			String message = TextUtil.t("study.basic", getCharacter(), specialization.getText());
			ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STUDY, getCharacter());
			return new MessageData(message, image, getCharacter().getBackground());
		}
		else {
			return new MessageData(TextUtil.t("idle.basic", getCharacter()), 
					ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()), 
					getCharacter().getBackground(), true);
		}
	}

	@Override
	public List<ModificationData> getStatModifications() {
		if (specialization != null) {
			List<ModificationData> modifications = new ArrayList<ModificationData>();
			modifications.add(new ModificationData(TargetType.ALL, -25, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.INTELLIGENCE));
			if (specialization != null) {
				if(getCharacters().get(0).getTraits().contains(Trait.CLEVER))
					modifications.add(new ModificationData(TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
				else if (getCharacters().get(0).getTraits().contains(Trait.STUPID))
					modifications.add(new ModificationData(TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));

				float change = (5f +  specialization.getAssociatedAttributes().size() -1) / specialization.getAssociatedAttributes().size();
				if(isHiddenTraining(specialization, getCharacter()) && Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.HIDDENLIBRARY))
					change*=450/100;
				else if(isAdvancedTraining(specialization, getCharacter()))
					change*=125/100;
				for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
					modifications.add(new ModificationData(TargetType.ALL, change, attributeType));
				}
			}
			return modifications;
		}
		else {
			return new Idle().getStatModifications();
		}
	}

	private boolean isBasicTraining(SpecializationType specialization, Charakter character) {
		if (character.getSpecializations().contains(specialization)) {
			return false;
		}

		if (specialization == SpecializationType.DOMINATRIX) {
			if (character.getAttribute(SpecializationAttribute.DOMINATE).getInternValue() > 1f) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
				if (character.getAttribute(attributeType).getInternValue() > 1.0f) {
					return false;
				}
			}
			return true;
		}
	}

	private boolean isAdvancedTraining(SpecializationType specialization, Charakter character) {
		for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
			if (character.getAttribute(attributeType).getInternValue() >= 20f) {
				return true;
			}
		}
		return false;
	}
	private boolean isHiddenTraining(SpecializationType specialization, Charakter character) {
		for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
			if (character.getAttribute(attributeType).getInternValue() >= 50f) {
				return true;
			}
		}
		return false;
	}
}