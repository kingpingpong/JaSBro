package jasbro.game.character.activities.sub.whore;

import jasbro.Util;
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

public class Struggle extends Whore {
	private MessageData messageData;
	private float actions = 1.5f;
	private boolean submit = false;
	
	@Override
	public void init() {
		super.init();
		setMinimumObedience(10);
		determineSubmit(getMainCustomer());
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
		message += "\n" + TextUtil.t("struggle.basic", getCharacter(), getMainCustomer());
		
		
		if (submit) {
			message += "\n" + TextUtil.t("struggle.result.lost", getCharacter());
			message += "\n" + TextUtil.t("struggle.bondage", getMainCustomer(), getCharacter());
		}
		else {
			message += "\n" + TextUtil.t("struggle.result.won", getCharacter(), getMainCustomer());
			message += "\n" + TextUtil.t("struggle.dominate", getCharacter());
		}
		
		message += "\n" + TextUtil.t("struggle.finish") + "\n";
		
		
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
			if (submit) {
				modifications.add(new ModificationData(TargetType.SLAVE, 0.5f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.ALL, 2f, SpecializationAttribute.DOMINATE));
				modifications.add(new ModificationData(TargetType.ALL, 0.5f, BaseAttributeTypes.STAMINA));
				modifications.add(new ModificationData(TargetType.ALL, -3f, EssentialAttributes.HEALTH));
				modifications.add(new ModificationData(TargetType.ALL, -10f, EssentialAttributes.ENERGY));
				modifications.add(new ModificationData(TargetType.ALL, 4f, Sextype.BONDAGE));
			} else {
				modifications.add(new ModificationData(TargetType.TRAINER, 0.5f, BaseAttributeTypes.COMMAND));
				modifications.add(new ModificationData(TargetType.ALL, 4f, SpecializationAttribute.DOMINATE));
				modifications.add(new ModificationData(TargetType.ALL, 0.5f, BaseAttributeTypes.STRENGTH));
				modifications.add(new ModificationData(TargetType.ALL, -1f, EssentialAttributes.HEALTH));
				modifications.add(new ModificationData(TargetType.ALL, -10f, EssentialAttributes.ENERGY));
				modifications.add(new ModificationData(TargetType.ALL, 2f, Sextype.BONDAGE));
			}
		}
		else {
			return new Idle().getStatModifications();
		}
		return modifications;
	}
	
	
	@Override
	public void perform() {
		if (getMainCustomer() != null) {
			int skill;
			
			if(submit)
				skill = getCharacter().getCommand() + getCharacter().getFinalValue(Sextype.BONDAGE) * 2;
			else
				skill = getCharacter().getCommand() + getCharacter().getFinalValue(SpecializationAttribute.DOMINATE) * 2;
			
			getMainCustomer().addToSatisfaction(skill, this);
			
			int pay = getMainCustomer().pay(getCharacter().getMoneyModifier() + 0.5f);
			modifyIncome(pay);
			
			getMessages().get(0).addToMessage("\n\n" + TextUtil.t("whore.end", getCharacter(),
					getMainCustomer(), getMainCustomer().getSatisfaction().getText(), pay));
		}
	}
	
	private void determineSubmit(Customer customer) {
		int bonus = 0;
		int bumBase = 2,
				businessBase = 2,
				celebrityBase = 4,
				lordBase = 4,
				merchantBase = 3,
				nobleBase = 3,
				peasantBase = 2,
				soldierBase = 7;
		
		switch(customer.getStatus()) {
			case DRUNK: bonus = -1; break;
			case HYPED: bonus = 1; break;
			case PISSED: bonus = 2; break;
			case SAD: bonus = -2; break;
			case SHYSTATUS: bonus = -1; break;
			case STRONGSTATUS: bonus = 3; break;
			case TIRED: bonus = -2; break;
			case VERYDRUNK: bonus = -3; break;
			default: bonus = 0; break;
		}
		
		switch(customer.getType()) {
		case BUM:
			if (Util.getInt(0, 10) < (bumBase + bonus)) {
				submit = true;
			} else {
				submit = false;
			} break;
		case BUSINESSMAN:
			if (Util.getInt(0, 10) < (businessBase + bonus)) {
				submit = true;
			} else {
				submit = false;
			} break;
		case CELEBRITY:
			if (Util.getInt(0, 10) < (celebrityBase + bonus)) {
				submit = true;
			} else {
				submit = false;
			} break;
		case LORD:
			if (Util.getInt(0, 10) < (lordBase + bonus)) {
				submit = true;
			} else {
				submit = false;
			} break;
		case MERCHANT:
			if (Util.getInt(0, 10) < (merchantBase + bonus)) {
				submit = true;
			} else {
				submit = false;
			} break;
		case MINORNOBLE:
			if (Util.getInt(0, 10) < (nobleBase + bonus)) {
				submit = true;
			} else {
				submit = false;
			} break;
		case PEASANT:
			if (Util.getInt(0, 10) < (peasantBase + bonus)) {
				submit = true;
			} else {
				submit = false;
			} break;
		case SOLDIER:
			if (Util.getInt(0, 10) < (soldierBase + bonus)) {
				submit = true;
			} else {
				submit = false;
			} break;
		default:
			submit = true; break;
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