package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterSchool {
	public List<Training> getTrainingOpportunities(Charakter character) {
		List<Training> possibleTraining = new ArrayList<CharacterSchool.Training>();
		
		for (SpecializationType specializationType : Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations()) {
		    if (specializationType != SpecializationType.TRAINER && 
		            specializationType != SpecializationType.SLAVE &&
		            specializationType != SpecializationType.UNDERAGE &&
		            specializationType.isTeachable() &&
		            !character.getSpecializations().contains(specializationType)) {
	            possibleTraining.add(new SpecializationBasicTraining(specializationType, character));
		    }
		}	
		
		for (SpecializationType specializationType : character.getSpecializations()) {
		    if (specializationType.isTeachable()) {
		        SpecializationTraining training = new SpecializationTraining(specializationType, character);
	            possibleTraining.add(training);
	            if (!training.fulfillsRequirements() && specializationType.getAssociatedAttributes().size() > 1) {
	                for (AttributeType attributeType : specializationType.getAssociatedAttributes()) {
	                    AttributeSpecializationTraining attributeSpecializationTraining = new AttributeSpecializationTraining(attributeType, character);
	                    if (attributeSpecializationTraining.fulfillsRequirements()) {
	                        boolean containsTraining = false;
	                        
	                        for (Training curTraining : possibleTraining) {
	                            if (curTraining instanceof AttributeSpecializationTraining) {
	                                AttributeSpecializationTraining curAttributeSpecializationTraining = (AttributeSpecializationTraining) curTraining;
	                                if (curAttributeSpecializationTraining.getAttributeType() == attributeType) {
	                                    containsTraining = true;
	                                    break;
	                                }
	                            }
	                        }
	                        
	                        if (!containsTraining) {
	                            possibleTraining.add(attributeSpecializationTraining);
	                        }
	                    }
	                }
	            }
		    }			
		}
		
		Collections.sort(possibleTraining);
		
		return possibleTraining;
	}
	

    public List<Training> getTrainingOpportunitiesHideUnavailable(Charakter selectedCharacter) {
        List<Training> trainingOptions = getTrainingOpportunities(selectedCharacter);
        for (int i = 0; i < trainingOptions.size(); i++) {
            Training training = trainingOptions.get(i);
            if (!training.fulfillsRequirements()) {
                trainingOptions.remove(training);
                i--;
            }
        }
        return trainingOptions;
    }
	
	public static abstract class Training implements Comparable<Training> {
		public abstract String getName();
		public abstract String getDescription();
		public abstract void apply();
		public abstract long getPrice();
		public abstract boolean fulfillsRequirements();
		
		@Override
		public int compareTo(Training o) {
			if (this instanceof AttributeSpecializationTraining) {
				return 1;
			}
			else if (o instanceof AttributeSpecializationTraining) {
				return -1;
			}
			else {
				if (this instanceof SpecializationBasicTraining && o instanceof SpecializationBasicTraining) {
					if (!fulfillsRequirements()) {
						return 1;
					}
					else if (!o.fulfillsRequirements()) {
						return -1;
					}
					else {
						return 0;
					}
				}
				else if (this instanceof SpecializationTraining && o instanceof SpecializationTraining) {
					if (!fulfillsRequirements()) {
						return 1;
					}
					else if (!o.fulfillsRequirements()) {
						return -1;
					}
					else {
						return 0;
					}
				}
				return 0;
			}
		}
	}
	
	public static class SpecializationBasicTraining extends Training {
		private SpecializationType specializationType;
		private Charakter character;
		
		public SpecializationBasicTraining(SpecializationType specializationType, Charakter character) {
			this.specializationType = specializationType;
			this.character = character;
		}

		@Override
		public String getName() {
			return TextUtil.t("school.basic."+specializationType.toString()+".title", character);
		}
		
		@Override
		public String getDescription() {
			String description = TextUtil.t("school.basic."+specializationType.toString()+".description", character);
			if (!fulfillsRequirements()) {
				description += " " + TextUtil.t("school.requirementsnotmet", character);
			}
			return description;
		}

		@Override
		public void apply() {
			Jasbro.getInstance().getData().spendMoney(getPrice(), "School");
			character.getSpecializations().add(specializationType);
		}

		@Override
		public long getPrice() {
			int amountSpecialisations = character.getSpecializations().size() - 1;
			switch (amountSpecialisations) {
			case 1: return 100;
			case 2: return 500;
			case 3: return 10000;
			case 4: return 50000;
			case 5: return 100000;
			case 6: return 120000;
			case 7: return 150000;
			default:
			}
			return 10l * ((amountSpecialisations * amountSpecialisations * amountSpecialisations * amountSpecialisations * amountSpecialisations) / 1000 * 1000);
		}

		@Override
		public boolean fulfillsRequirements() {
			int amount = 0;
			if (specializationType == SpecializationType.DOMINATRIX) {
				if (character.getAttribute(SpecializationAttribute.DOMINATE).getInternValue() > 0.5f) {
					return true;
				}
				return false;
			}
			else {
				for (AttributeType attribute : specializationType.getAssociatedAttributes()) {
					if (character.getAttribute(attribute).getInternValue() > 0.5f) {
						return true;
					}
					amount++;
				}
				if (amount == 0) {
					return true;
				}
				else {
					return false;
				}
			}
		}		
	}
	
	public static class SpecializationTraining extends Training {
		private SpecializationType specializationType;
		private Charakter character;
		
		public SpecializationTraining(SpecializationType specializationType, Charakter character) {
			this.specializationType = specializationType;
			this.character = character;
		}

		@Override
		public String getName() {
			Object arguments[] = {specializationType.getText(), specializationType.getTrainingLevel(character)};
			return TextUtil.t("school.specializationtraining.name", character, arguments);
		}
		
		@Override
		public String getDescription() {
			AttributeType attributeType = character.getAttribute(specializationType.getAssociatedAttributes().get(0)).getAttributeType();
			int level = specializationType.getTrainingLevel(character);
			Object arguments [] = {TextUtil.listAttributes(specializationType.getAssociatedAttributes()), (attributeType.getDefaultMax() + level * attributeType.getRaiseMaxBy())};
			String description = TextUtil.t("school.specializationtraining.description", character, arguments);
			if (!fulfillsRequirements()) {				
				arguments[0] = attributeType.getDefaultMax() - attributeType.getRaiseMaxBy() + attributeType.getRaiseMaxBy() * (specializationType.getTrainingLevel(character) - 1 );
				description += " " + TextUtil.t("school.specializationtraining.requirementsnotmet", character, arguments);
			}
			return description;
		}

		@Override
		public void apply() {
			Jasbro.getInstance().getData().spendMoney(getPrice(), "School");
			int level = specializationType.getTrainingLevel(character);
			for (AttributeType attributeType : specializationType.getAssociatedAttributes()) {
				int newMax = attributeType.getDefaultMax() + level * attributeType.getRaiseMaxBy();
				Attribute attribute = character.getAttribute(attributeType);
				while (attribute.getMaxValue() < newMax) {
					attribute.setMaxValue(attribute.getMaxValue() + attributeType.getRaiseMaxBy());
				}
			}
		}

		@Override
		public long getPrice() {
			int level = specializationType.getTrainingLevel(character);
            switch (level) {
            case 1: return 1000;
            case 2: return 10000;
            case 3: return 30000;
            case 4: return 50000;
            case 5: return 100000;
            case 6: return 150000;
            case 7: return 200000;
            default:
            }
            return 100l * ((level * level * level * level) / 1000 * 1000);
		}

		@Override
		public boolean fulfillsRequirements() {
			int sum = 0;
			int amount = 0;
			Attribute attribute = null;
			for (AttributeType attributeType : specializationType.getAssociatedAttributes()) {
				attribute = character.getAttribute(attributeType);
				sum += attribute.getInternValue();
				amount++;
			}
			if (sum / amount >= attribute.getAttributeType().getDefaultMax() - attribute.getAttributeType().getRaiseMaxBy() + attribute.getAttributeType().getRaiseMaxBy() * (specializationType.getTrainingLevel(character) - 1 )) {
				return true;
			}
			else {
				return false;
			}
		}

        public SpecializationType getSpecializationType() {
            return specializationType;
        }
	}
	
	public static class AttributeSpecializationTraining extends Training {
		private AttributeType attributeType;
		private Charakter character;
		
		public AttributeSpecializationTraining(AttributeType attributeType, Charakter character) {
			this.attributeType = attributeType;
			this.character = character;
		}

		@Override
		public String getName() {
			Object arguments[] = {attributeType.getText()};
			return TextUtil.t("school.specialize", arguments);
		}
		
		@Override
		public String getDescription() {
			Object arguments[] = {attributeType.getText()};
			String description = TextUtil.t("school.specialice.description", arguments);
			if (!fulfillsRequirements()) {
				description += " " + character.getName() + " needs more training to learn this";
			}
			return description;
		}

		@Override
		public void apply() {
			Jasbro.getInstance().getData().spendMoney(getPrice(), "School");
			Attribute attribute = character.getAttribute(attributeType);
			attribute.setMaxValue(attribute.getMaxValue() + attributeType.getRaiseMaxBy());
		}

		@Override
		public long getPrice() {
			Attribute attribute = character.getAttribute(attributeType);
			int level = 1 + (attribute.getMaxValue() - attributeType.getDefaultMax()) / attributeType.getRaiseMaxBy();
            switch (level) {
            case 1: return 1000;
            case 2: return 5000;
            case 3: return 10000;
            case 4: return 30000;
            case 5: return 50000;
            case 6: return 100000;
            case 7: return 150000;
            default:
            }
            return 100l * ((level * level * level * level) / 1000 * 1000);
		}

		@Override
		public boolean fulfillsRequirements() {
			Attribute attribute = character.getAttribute(attributeType);
			return attribute.getMaxValue() == attribute.getInternValue();
		}

		public AttributeType getAttributeType() {
			return attributeType;
		}		
		
	}
}
