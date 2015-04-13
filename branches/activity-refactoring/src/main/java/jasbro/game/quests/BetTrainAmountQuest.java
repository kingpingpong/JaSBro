package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.conditions.Questtimer;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class BetTrainAmountQuest extends Quest {
	private final static int amount[] = {5, 15, 30};
	private final static int wages[] = {1000, 10000, 100000};
    private AttributeType attribute;
    private Integer targetAmount;
    private Charakter slave;
    private int timeRemaining;
    private Reward reward;
	
    @Override
    public List<QuestStage> getInitStages() {
        List<QuestStage> questStages = new ArrayList<QuestStage>();
        questStages.add(new PassiveStage());
        questStages.add(new RunningBetStage());
        return questStages;
    }
    
	private class PassiveStage extends QuestStage {
	    	    
		@Override
		public void handleEvent(MyEvent e, Quest quest) {
			if (e.getType() == EventType.NEXTDAY) {
				if (Util.getInt(0, 100) < 3) {
					Charakter character = null;
					List<Charakter> characters = Jasbro.getInstance().getData().getCharacters();
					for (int i = 0; i < 10; i++) {
						character = characters.get(Util.getInt(0, characters.size()));
						if (character.getOwnership() == Ownership.OWNED && character.getType() == CharacterType.SLAVE) {
							break;
						}
						else {
							character = null;
						}
					}
					
					if (character != null) {
						SpecializationType specializationType;
						AttributeType attribute;
						int amountRequired = 0;
						do {
							specializationType = SpecializationType.values()
									[Util.getRnd().nextInt(SpecializationType.values().length)];
							if (specializationType == SpecializationType.TRAINER ||
							        specializationType == SpecializationType.UNDERAGE) {
								continue;
							}

							attribute = specializationType.getAssociatedAttributes().get(
								Util.getRnd().nextInt(specializationType.getAssociatedAttributes().size()));
							
							if (Jasbro.getInstance().getData().getDay() < 30) {
								if	((specializationType != SpecializationType.SLAVE && specializationType != SpecializationType.SEX && 
										specializationType != SpecializationType.MAID) || 
										(specializationType == SpecializationType.MAID && attribute == SpecializationAttribute.COOKING)) {
									continue;
								}
							}
							break;
						} while (true);
						
						if (!character.getSpecializations().contains(specializationType)) {
							amountRequired += amount[0];
						}
						int finalAmount[] = {amount[0] + amountRequired, amount[1] + amountRequired, amount[2] + amountRequired};
						if (attribute instanceof BaseAttributeTypes) {
							finalAmount[0] = 2;
							finalAmount[1] = 4;
							finalAmount[2] = 7;
						}
						
						
						List<SelectionData<Integer>> options = new ArrayList<SelectionData<Integer>>();
						Object arguments[] = {wages[0], attribute.getText(), finalAmount[0]};
						options.add(new SelectionData<Integer>(0, TextUtil.t("quests.betquest.lowbet", arguments) + " " + TextUtil.t("quests.betquest.details", character, arguments), 
								Jasbro.getInstance().getData().canAfford(wages[0])));
						arguments[0] = wages[1];
						arguments[1] = attribute.getText();
						arguments[2] = finalAmount[1];
						options.add(new SelectionData<Integer>(1, TextUtil.t("quests.betquest.mediumbet", arguments) + " " + TextUtil.t("quests.betquest.details", character, arguments), 
								Jasbro.getInstance().getData().canAfford(wages[1])));
						arguments[0] = wages[2];
						arguments[1] = attribute.getText();
						arguments[2] = finalAmount[2];
						options.add(new SelectionData<Integer>(2, TextUtil.t("quests.betquest.highbet", arguments) + " " + TextUtil.t("quests.betquest.details", character, arguments), 
								Jasbro.getInstance().getData().canAfford(wages[2])));
						options.add(new SelectionData<Integer>(3, TextUtil.t("quests.betquest.nobet")));
						
						SelectionData<Integer> selectedOption = new SelectionScreen<Integer>()
								.select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character), null,
								character.getBackground(), TextUtil.t("quests.betquest.intro", character, arguments));
						Integer selected = selectedOption.getSelectionObject();
						
						if (selected != 3) {
							Reward reward = new Reward(wages[selected] * 2, BetTrainAmountQuest.this);
                            if (selected == 2) {
                                reward.setPerkPoints(1);
                            }
							Jasbro.getInstance().getData().spendMoney(wages[selected], BetTrainAmountQuest.this);
							BetTrainAmountQuest.this.setCurrentStage(1);
							slave = character;
							timeRemaining = 7;
							BetTrainAmountQuest.this.attribute = attribute;
							BetTrainAmountQuest.this.targetAmount = character.getFinalValue(attribute) + finalAmount[selected];
							BetTrainAmountQuest.this.reward = reward;
							setActive();
						}
					}
				}
			}
		}
	}
	
	
	
	private class RunningBetStage extends QuestStage {
		
		@Override
		public void init(Quest quest) {
			String text = TextUtil.t("quest.startet") + " " + getTitle(quest) + "\n" + TextUtil.t("quests.betquest.start");
			new MessageScreen(text, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, slave), slave.getBackground());
			getSlave().addCondition(new Questtimer(getTimeRemaining(), BetTrainAmountQuest.this));
		}
		
		@Override
		public String getTitle(Quest quest) {
			return TextUtil.t("quests.betquest.title", getSlave());
		}
		
		@Override
		public String getDescription(Quest quest) {
			Object arguments[] = {attribute.getText(), targetAmount};
			Object argumentsTime[] = {getTimeRemaining()};
			String text = TextUtil.t("quests.betquest.description", slave, arguments) + "\n";
			text +=	TextUtil.t("quest.time", argumentsTime) + "\n";
			text += reward.getRewardDescription();
			return text;
		}
		
		public Charakter getSlave() {
			return slave;
		}

		public int getTimeRemaining() {
			return timeRemaining;
		}
		
		@Override
		public void handleEvent(MyEvent e, Quest quest) {
			if (e.getType() == EventType.CHARACTERLOST && e.getSource() == slave) {
				setResolved();
				//Do something?
			}
			else if (e.getType() == EventType.NEXTDAY) {
				timeRemaining--;
				if (getTimeRemaining() <= 0) {
					if (getSlave().getFinalValue(attribute) >= targetAmount) {
						reward.applyReward(getSlave());
						Object arguments [] = {reward.getRewardMoney()};
						String message = TextUtil.t("quests.betquest.won", getSlave(), arguments);
						new MessageScreen(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, 
								getSlave()), getSlave().getBackground(), true);
						setResolved();
					}
					else {
					    Jasbro.getInstance().getData().getProtagonist().getFame().modifyFame(-reward.getPenalty());
						Object arguments[] = {reward.getRewardMoney()};
						String message = TextUtil.t("quests.betquest.lost", getSlave(), arguments) + " ";
						new MessageScreen(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getSlave()), 
								getSlave().getBackground(), true);
						setResolved();
					}
				}
			}
		}
	}
	
	@Override
	public void setResolved() {
		setCurrentStage(0);
		super.setResolved();
		Jasbro.getInstance().getData().getQuestManager().getInactiveQuests().add(this);
	}
	
	@Override
	public String toString() {
		return getTitle();
	}

    public boolean showInQuestLog() {
        return getCurrentStage().showInQuestlog(this);
    }
}
