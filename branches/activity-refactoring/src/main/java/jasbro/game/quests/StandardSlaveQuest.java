package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.conditions.Questtimer;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.Fame;
import jasbro.game.events.business.SpawnData;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class StandardSlaveQuest extends Quest {

    private Charakter slave;
    private int timeRemaining;
    private CharacterGoal goal;
    private Reward reward;
    private String clientType;
    
	public StandardSlaveQuest(Charakter slave, int time, CharacterGoal goal, Reward reward) {
		reward.setQuest(this);
        this.slave = slave;
        this.timeRemaining = time;
        this.goal = goal;
        this.reward = reward;
	}
	
    @Override
    public List<QuestStage> getInitStages() {
        List<QuestStage> questStages = new ArrayList<QuestStage>();
        questStages.add(new GirlQuestStage());
        return questStages;
    }
	
	private class GirlQuestStage extends QuestStage {


		@Override
		public void init(Quest quest) {
			String text = TextUtil.t("quest.startet") + " " + getTitle(quest) + "\n" + getDescription(quest);
			new MessageScreen(text, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, slave), slave.getBackground());
			Jasbro.getInstance().getData().getCharacters().add(getSlave());
			getSlave().addCondition(new Questtimer(getTimeRemaining(), StandardSlaveQuest.this));
		}
		
		@Override
		public String getTitle(Quest quest) {
			return TextUtil.t("quests.standardgirlquest.title", getSlave());
		}
		
		@Override
		public String getDescription(Quest quest) {
			String argumentsTime[] = {getTimeRemaining() + ""};
			String argumentsClient[] = {getClientType()};
			String text = TextUtil.t("quests.standardgirlquest.description", argumentsClient) + "\n";
			text +=	TextUtil.t("quest.time", argumentsTime) + "\n";
			text += goal.getDescription();
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
			GameData gameData = Jasbro.getInstance().getData();
			if (e.getType() == EventType.CHARACTERDEATH && e.getSource() == slave) {
				long value = slave.calculateValue();
				long cost = ((value * 2) / 100) * 100;
				gameData.spendMoney(cost, this);
				gameData.getProtagonist().getFame().modifyFame(-1000);
				String arguments[] = {""+cost};
				new MessageScreen(TextUtil.t("quests.standardgirlquest.death", arguments), 
						new ImageData("images/backgrounds/coffin.jpg"), null);
			}
			else if (e.getType() == EventType.NEXTDAY) {
				timeRemaining--;
				if (getTimeRemaining() <= 0) {
					finish(quest);
				}
			}
		}

		@Override
		public void finish(Quest quest) {
			GameData gameData = Jasbro.getInstance().getData();
			Jasbro.getInstance().removeCharacter(slave);
			if (getGoal().goalReached(getSlave())) {
				reward.applyReward(null);
				String message = TextUtil.t("quests.standardgirlquest.success", getSlave());
				message += " " + reward.getSuccessMessage();
				if (!gameData.getCharacters().contains(slave)) {
					message +=  " " + TextUtil.t("quests.standardgirlquest.slavereturned", getSlave());
				}
				new MessageScreen(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, 
						getSlave()), getSlave().getBackground(), true);
				Jasbro.getInstance().getData().getQuestManager().setSolved(StandardSlaveQuest.this, 5);
			}
			else {
				long penalty = reward.getPenalty();
				gameData.getProtagonist().getFame().modifyFame(-penalty);
				gameData.spendMoney(penalty, StandardSlaveQuest.this.getTitle());
				String arguments[] = {penalty+""};
				String message = TextUtil.t("quests.standardgirlquest.failure", getSlave(), arguments) + " ";
				message += TextUtil.t("quests.standardgirlquest.slavereturned.female", getSlave());
				new MessageScreen(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getSlave()), 
						getSlave().getBackground(), true);
				Jasbro.getInstance().getData().getQuestManager().setSolved(StandardSlaveQuest.this, -15);
			}
		}

		public String getClientType() {
			if (clientType == null) {
				SpawnData spawnData = new SpawnData();
				Customer customer = spawnData.spawn(1, new Fame()).get(0);
				clientType = customer.getName();
			}
			return clientType;
		}

		public CharacterGoal getGoal() {
			return goal;
		}
		
		@Override
		public boolean canFinishEarly(Quest quest) {
			return getGoal().goalReached(getSlave());
		}
	}
	
	public static StandardSlaveQuest generate(int difficultyModifier) {
		Charakter character = Jasbro.getInstance().generateBasicSlave();
		CharacterGoal goal = new CharacterGoal();
		SpecializationType specializationTypes[] = SpecializationType.values();

		int modifier = difficultyModifier / 3 + Util.getInt(-5, 5);
	    while (modifier > 100 && Util.getInt(0, 100) < 80) {
            modifier = modifier / Util.getInt(2, 4) + Util.getInt(-5, 5);
	    }
		int curmodifier = modifier;
		do {
			SpecializationType specializationType;
			do {
				specializationType = specializationTypes[Util.getRnd().nextInt(specializationTypes.length)];
				
				if (specializationType == SpecializationType.SLAVE || 
				        specializationType == SpecializationType.TRAINER ||
				        specializationType == SpecializationType.UNDERAGE ||
				        !specializationType.isTeachable() ||
						goal.getSpecializations().contains(specializationType)) {
					continue;
				}
				
				if (Jasbro.getInstance().getData().getDay() < 10) {
				    if (specializationType != SpecializationType.MAID && specializationType != SpecializationType.WHORE &&
				            specializationType != SpecializationType.SEX) {
				        continue;
				    }
				}
				
				break;
			}
			while (true);
			
			goal.getSpecializations().add(specializationType);
			if (goal.getSpecializations().size() > 1) {
				curmodifier -= 10;
				if (curmodifier < 0) {
					curmodifier = 0;
				}
			}
			
			while (character.getSpecializations().contains(specializationType) && curmodifier <= 5) {
			    curmodifier += 5;
			}
			
			if (curmodifier > 5) {
				int amountAttributes = specializationType.getAssociatedAttributes().size();
				int i = 0;
				do {
					int goalAttribute = Util.getInt(1, curmodifier);
					curmodifier -= goalAttribute;					
					if (curmodifier < 10) {
						goalAttribute += curmodifier;
						curmodifier = 0;
					}					
					AttributeType attribute = specializationType.getAssociatedAttributes().get(Util.getInt(0, amountAttributes));
					if (goal.getAttributeValueMap().containsKey(attribute)) {
						goalAttribute += goal.getAttributeValueMap().get(attribute);
					}
					if (goalAttribute > 0) {
						goal.getAttributeValueMap().put(attribute, goalAttribute + 1);
					}
					i++;
				}
				while (i < amountAttributes * 2 && curmodifier > 0);
			}
			else {
				curmodifier = 0;
			}
		}
		while (curmodifier > 0 && specializationTypes.length - 2 >= goal.getSpecializations().size());		
		
		int timeModifier = Util.getInt(-2, 4);
		float time = timeModifier + 10 * goal.getSpecializations().size();
		
		float attributeToTimeProportion;
		if (difficultyModifier < 100) {
			attributeToTimeProportion = 1.1f;
		}
		else if (difficultyModifier < 500) {
			attributeToTimeProportion = 1.5f;
		}
		else {
		    attributeToTimeProportion = 2f;
		}
		
		int sumAttributes = 0;
		for (AttributeType attributeType : goal.getAttributeValueMap().keySet()) {
			time += goal.getAttributeValueMap().get(attributeType) / attributeToTimeProportion;
			sumAttributes += goal.getAttributeValueMap().get(attributeType);
		}
		int reward = (int) ( sumAttributes * attributeToTimeProportion * attributeToTimeProportion * attributeToTimeProportion 
				* attributeToTimeProportion * attributeToTimeProportion *
				(goal.getSpecializations().size() - 0.5f * (goal.getSpecializations().size() - 1) ) *
				(goal.getAttributeValueMap().keySet().size() - 0.5f * (goal.getAttributeValueMap().keySet().size() - 1) ) * 
				(time / 25f)
				+ sumAttributes * 80
				+ (goal.getAttributeValueMap().keySet().size() - 1) * 1000
				+ (goal.getSpecializations().size() - 1) * 5000
				+ 400 - timeModifier * 100 );
		
		//add specialization costs
		int specializationsToTrain = 0;
		for (SpecializationType specializationType : goal.getSpecializations()) {
		    if (!character.getSpecializations().contains(specializationType)) {
		        specializationsToTrain++;
		    }
		}
		int amountSpecializations = character.getSpecializations().size()-1;
		for (int i = 0; i < specializationsToTrain; i++) {
		    reward += 100 * amountSpecializations * amountSpecializations * amountSpecializations * amountSpecializations;
		    amountSpecializations++;
		}
		
		
		if (goal.getSpecializations().size() == 1 && goal.getSpecializations().get(0) == SpecializationType.WHORE) {
			reward = reward / 2;
		}
		if (goal.getSpecializations().contains(SpecializationType.KINKYSEX)) {
			reward = reward + reward / 2;
			time = time + time / 2;
		}
		
		if (reward < 100) {
			reward = 100;
		}
		
		return new StandardSlaveQuest(character, (int) time, goal, new Reward(reward, null));
	}
	
}
