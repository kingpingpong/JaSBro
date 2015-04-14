package jasbro.game.character.conditions;

import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.warnings.Severity;
import jasbro.game.character.warnings.Warning;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.List;

public abstract class Illness extends Condition {
	protected int remainingTime = 10;
	
	public Illness() {
	}

	@Override
	public void init() {
	    super.init();
	    getCharacter().getCounter().add(CounterNames.SICK.toString());
	}
	
	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public static class Flu extends Illness {
		private boolean showMessage = true;
		public Flu() {
		}
		public Flu(boolean showMessage) {
			this.showMessage = showMessage;
		}
		
		@Override
		public void init() {
			super.init();
			setRemainingTime(Util.getInt(6, 12));
			AttributeModification mod = new AttributeModification(-5, EssentialAttributes.HEALTH, getCharacter());
			mod.applyModification();
			if (showMessage && getCharacter().getConditions().contains(this)) {
				new MessageScreen(getInfectMessage());
			}
		}
		
		public MessageData getInfectMessage() {
		    return new MessageData(getTextStart(), 
                    ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, getCharacter()), 
                    getCharacter().getBackground(), true);
		}

		public String getTextStart() {
			return TextUtil.t("flu.start", getCharacter());
		}
		
		@Override
		public String getName() {
			return "Flu";
		}

		@Override
		public ImageData getIcon() {
			return new ImageData("images/icons/flu.png");
		}

		@Override
		public String getDescription() {
			return getCharacter().getName() +" has the Flu and can not work.";
		}
		
		@Override
		public void handleEvent(MyEvent e) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getPlannedActivity().getType() != ActivityType.SLEEP &&
						activity.getPlannedActivity().getType() != ActivityType.SOAK && 
						activity.getPlannedActivity().getType() != ActivityType.NURSE) {
					new MessageScreen(TextUtil.t("illness.basic", getCharacter()), 
							ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, getCharacter()), 
							getCharacter().getBackground());
					activity.setAbort(true);
				}
				else if (activity.getPlannedActivity().getType() == ActivityType.NURSE) {
					remainingTime--;
					remainingTime--;
				}
				else if (activity.getPlannedActivity().getType() == ActivityType.SOAK) {
					remainingTime--;
					remainingTime--;
					MessageData message = activity.getMessages().get(0);
					message.addToMessage(TextUtil.t("flu.soak", getCharacter()));
				}
				else {
					MessageData message = activity.getMessages().get(0);
					message.addToMessage(TextUtil.t("flu.sleep", getCharacter()));
					remainingTime--;
				}
			}
			else if (e.getType() == EventType.NEXTSHIFT) {
				remainingTime--;
				if (remainingTime <= 0) {
					getCharacter().getConditions().remove(this);
					new MessageScreen(TextUtil.t("illness.cured", getCharacter()), 
							ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()), 
							getCharacter().getBackground());
				}
			}
		}
		
		@Override
		public void modifyWarnings(List<Warning> warnings) {
            warnings.add(new Warning(Severity.WARN, getDescription()));
		}
	}
	
	
	public static class Smallpox extends Illness {
		private boolean showMessage = true;
		public Smallpox() {
		}
		public Smallpox(boolean showMessage) {
			this.showMessage = showMessage;
		}
		
		@Override
		public void init() {
			super.init();
			setRemainingTime(20);
			if (getCharacter().getConditions().contains(this) && showMessage) {
				new MessageScreen(getInfectMessage());
			}
		}
		
        public MessageData getInfectMessage() {
            return new MessageData(getTextStart(), 
                    ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, getCharacter()), 
                    getCharacter().getBackground(), true);
        }

		public String getTextStart() {
			return TextUtil.t("smallpox.start", getCharacter());
		}
		
		@Override
		public String getName() {
			return TextUtil.t("smallpox");
		}

		@Override
		public ImageData getIcon() {
			return new ImageData("images/icons/biohazard.png");
		}

		@Override
		public String getDescription() {
			return TextUtil.t("smallpox.description", getCharacter());
		}
		
		@Override
		public void handleEvent(MyEvent e) {
			if (e.getType() == EventType.ACTIVITYPERFORMED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getPlannedActivity().getType() == ActivityType.NURSE) {
					remainingTime-= 5;
					MessageData message = activity.getMessages().get(0);
					message.addToMessage(TextUtil.t("smallpox.medicalAttention", getCharacter()));
					activity.getAttributeModifications().add(new AttributeModification(15, EssentialAttributes.HEALTH, getCharacter()));
				}
				else if (activity.getPlannedActivity().getType() == ActivityType.SLEEP) {
					remainingTime--;
					MessageData message = activity.getMessages().get(0);
					message.addToMessage(TextUtil.t("smallpox.sleep", getCharacter()));
					activity.getAttributeModifications().add(new AttributeModification(5, EssentialAttributes.HEALTH, getCharacter()));
					infect(activity);
				}				
				else {
					infect(activity);
				}
				if (!(activity instanceof Whore)) {
					activity.getAttributeModifications().add(new AttributeModification(-40, EssentialAttributes.HEALTH, getCharacter()));
				}
				else {
					activity.getAttributeModifications().add(new AttributeModification(-10, EssentialAttributes.HEALTH, getCharacter()));
				}
			}
			else if (e.getType() == EventType.NEXTSHIFT) {
				remainingTime--;
				if (remainingTime <= 0) {
					getCharacter().getConditions().remove(this);
					new MessageScreen(TextUtil.t("smallpox.cured", getCharacter()), 
							ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()), 
							getCharacter().getBackground());
				}
			}
		}
		
		private void infect(RunningActivity activity) {
			for (Charakter character : activity.getCharacterLocation().getCurrentUsage().getCharacters()) {
				if (Util.getInt(0, 100) < 80) {
				    Smallpox condition = new Smallpox(false);
					character.addCondition(condition);
					if (character.getConditions().contains(condition)) {
					    activity.getMessages().add(condition.getInfectMessage());
					}
				}
			}
		}
		
		@Override
		public float getAttributeModifier(Attribute attribute) {
			if (attribute.getAttributeType() == BaseAttributeTypes.CHARISMA) {
				return -2;
			}
			else {
				return 0;
			}
		}
		
		@Override
		public void modifyWarnings(List<Warning> warnings) {
		    warnings.add(new Warning(Severity.DANGER, getDescription()));
		}
        public void setShowMessage(boolean showMessage) {
            this.showMessage = showMessage;
        }
		
		
	}
}
