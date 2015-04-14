package jasbro.game.character.activities.sub.whore;

import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Suck extends Whore {
	
	@Override
	public int rateCustomer(Customer customer) {
		int rating = super.rateCustomer(customer);
		if (customer.getPreferredSextype() == Sextype.ORAL && (customer.getGender()==Gender.MALE || customer.getGender()==Gender.FUTA)) {
			return rating * 4;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public String checkPossible(RunningActivity activity, Charakter whore) {
		return "";
	}
	
	@Override
	public void init() {
		super.init();
	}
	
    @Override
    public MessageData getBaseMessage() {
        MessageData message = super.getBaseMessage();
        message.addToMessage(TextUtil.t("gloryhole.basic", getCharacter(), getMainCustomer()));

        List<ImageTag> tags = getCharacter().getBaseTags();
        tags.add(0, ImageTag.BLOWJOB);
        tags.add(1, ImageTag.GLORYHOLE);
        if (getMainCustomer().getType() == CustomerType.GROUP) {
            tags.add(2, ImageTag.BUKKAKE);
        }
        tags.addAll(ImageTag.getAssociatedImageTags(getCharacter(), getMainCustomer()));

        ImageData imageData = ImageUtil.getInstance().getImageDataByTags(tags, getCharacter().getImages());
        message.setImage(imageData);
        return message;
    }
	
	@Override
	public List<Sextype> getPossibleSextypes(Customer customer) {
		List<Sextype> sextypes = new ArrayList<Sextype>();
		sextypes.add(Sextype.ORAL);
		return sextypes;
	}
	
    @Override
    public void perform() {
        if (getMainCustomer().getPreferredSextype() == Sextype.ORAL) {
            getMainCustomer().addToSatisfaction(getCharacter().getFinalValue(Sextype.ORAL) / 4, this);
        }
        getMainCustomer().changePayModifier(-0.33f);
        super.perform();
    }
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modificationData  = new ArrayList<ModificationData>();
		modificationData.add(new ModificationData(TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
		modificationData.add(new ModificationData(TargetType.TRAINER, -0.2f, BaseAttributeTypes.OBEDIENCE));
		modificationData.add(new ModificationData(TargetType.ALL, 1.0f, Sextype.ORAL));
		modificationData.add(new ModificationData(TargetType.ALL, -4, EssentialAttributes.ENERGY));
		return modificationData;
	}
	
	@Override
	public Float getAmountActions() {
		return 0.7f;
	}
	
	
}
