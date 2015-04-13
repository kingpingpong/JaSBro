package jasbro.game.character.activities.sub.business;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.housing.House;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Activity class for massaging customers in the spa area and at the beach.
 */
public class Massage extends RunningActivity implements BusinessMainActivity  {

	@SuppressWarnings("unused")
    private final Logger log = Logger.getLogger(Massage.class);
	
	private MessageData messageData;

	/**
	 * Initialization figures out what each slave does exactly.
	 */
	@Override
	public void init() {
		Charakter slave = getCharacters().get(0);
		String message;
        
        message=TextUtil.t("massage.service.prepare", slave, getMainCustomer());
		messageData = new MessageData(message, null, getBackground());
	}

	/**
	 * Massage to raise satisfaction. Get paid.
	 */
	@Override
	public void perform() {
		for (Charakter character: getCharacters()) {
			int skill; 

			skill = character.getCharisma() / 4 + getCharacter().getFinalValue(SpecializationAttribute.WELLNESS) + getCharacter().getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 2;
			skill = skill / 2;

            int payment = getMainCustomer().pay(getCharacter().getMoneyModifier());
	        modifyIncome(payment);
	    	
	    	Object arguments[] = {getMainCustomer().getSatisfaction().getText(), getIncome()};
	        messageData.addToMessage("\n\n" + TextUtil.t("massage.result", getCharacter(), getMainCustomer(), arguments));
	        
	        if (getHouse() != null) {
	            House house = getHouse();
	        	house.modDirt(1);
	        }
	        
		}
	}

    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        for (Charakter character : getCharacters()) {
            modifications.add(new ModificationData(TargetType.SINGLE, character, -30, EssentialAttributes.ENERGY));
            modifications.add(new ModificationData(TargetType.SINGLE, character, 0.01f, BaseAttributeTypes.OBEDIENCE));
            
			modifications.add(new ModificationData(TargetType.SINGLE, character, 0.75f, SpecializationAttribute.WELLNESS));
			modifications.add(new ModificationData(TargetType.SINGLE, character, 0.25f, SpecializationAttribute.MEDICALKNOWLEDGE));
        }
        modifications.add(new ModificationData(TargetType.TRAINER, -0.1f, BaseAttributeTypes.COMMAND));
        return modifications;
    }

	
	@Override
	public MessageData getBaseMessage() {
		List<ImageTag> tags = getCharacter().getBaseTags();
		
		tags.addAll(ImageTag.getAssociatedImageTags(getCharacter(), getMainCustomer()));
		
		if (tags.contains(ImageTag.FUTA)) {
			tags.add(0, ImageTag.FUTA);
		}
		if (tags.contains(ImageTag.LESBIAN)) {
			tags.add(0, ImageTag.LESBIAN);
		}

		final ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, getCharacter().getImages());
		messageData.setImage(image);

		String messageText=TextUtil.t("massage.service.basic", getCharacter(), getMainCustomer(), null);
		messageData.addToMessage(messageText);
        
        return this.messageData;
	}

	@Override
	public int rateCustomer(Customer customer) {
		int rating;
		if (customer.getType()==CustomerType.GROUP) {
			rating=0;
		} else {
			rating=1 + getCharacter().getFinalValue(SpecializationAttribute.WELLNESS) / 5;
		}
		return rating;
	}
	
}
