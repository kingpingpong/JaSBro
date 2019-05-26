package jasbro.game.character.activities.sub.whore;

import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Dominate extends Whore {
	private MessageData messageData;
	private float actions = 2.0f;
	
	@Override
	public void init() {
		super.init();
		
		setMinimumObedience(0);
		setExecutionTime(45);
		setCooldownTime(45);
	}
	
	@Override
	public MessageData getBaseMessage() {
		
		String house = getHouse().getName();
		String customer = getMainCustomer().getName();
		String mood = getMainCustomer().getStatusName();
		
		String message;
		if (getHouse().getInternName() == null || getHouse().getInternName().trim().equals("")) {
			message = TextUtil.t("whore.basic1", mood, customer, house) + " ";
		}
		else {
			message = TextUtil.t("whore.basic2", mood, customer, house) + " ";
		}
		
		message += TextUtil.t("whore.service", getCharacter(), getMainCustomer());
		message += "\n";
		
		message += TextUtil.t("dominate.basic", getCharacter(), getMainCustomer());
		
		List<ImageTag> tags = getCharacter().getBaseTags();
		tags.add(0, ImageTag.DOMINATRIX);
		if (getMainCustomer().getType() == CustomerType.GROUP) {
			tags.add(ImageTag.GROUP);
		}
		
		messageData = new MessageData(message, ImageUtil.getInstance().getImageDataByTags(tags, getCharacter().getImages()), 
				getBackground());
		return messageData;
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		if (getMainCustomer() != null) {
			modifications.add(new ModificationData(TargetType.ALL, -10, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.TRAINER, 0.1f, BaseAttributeTypes.COMMAND));
			modifications.add(new ModificationData(TargetType.ALL, 2f, Sextype.BONDAGE));
			modifications.add(new ModificationData(TargetType.ALL, 2f, SpecializationAttribute.DOMINATE));
		}
		else {
			return new Idle().getStatModifications();
		}
		return modifications;
	}
	
	
	@Override
	public void perform() {
		if (getMainCustomer() != null) {
			int skill = getCharacter().getCommand() + getCharacter().getFinalValue(SpecializationAttribute.DOMINATE)
					+ getCharacter().getFinalValue(Sextype.BONDAGE) / 4;
			
			getMainCustomer().addToSatisfaction(skill, this);
			setAmountActions(getExecutionTime()+getCooldownTime());
			int pay = getMainCustomer().pay(getCharacter().getMoneyModifier() + 0.3f);
			modifyIncome(pay);
			getMessages().get(0).addToMessage("\n\n" + TextUtil.t("whore.end", getCharacter(),
					getMainCustomer(), getMainCustomer().getSatisfaction().getText(), pay));   
		}
	}
	
	@Override
	public List<Sextype> getPossibleSextypes(Customer customer) {
		List<Sextype> sextypes = new ArrayList<Sextype>();
		sextypes.add(Sextype.BONDAGE);
		return sextypes;
	}
	
	@Override
	public int rateCustomer(Customer customer) {
		int rating = super.rateCustomer(customer);
		if (customer.getPreferredSextype() == Sextype.BONDAGE) {
			return rating * 4;
		}
		else {
			return 0;
		}
	}
	
	public void setActionCost(float cost) {
		actions = cost;
	}
	
	@Override
	public Float getAmountActions() {
		return actions;
	}
}