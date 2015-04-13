package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.events.business.Fame;
import jasbro.texts.TextUtil;

public class Reward {
	private int rewardMoney;
	private Charakter rewardCharacter;
	private Quest quest;
	private int perkPoints;
	
	public Reward(int amountMoney, Quest quest) {
		this.rewardMoney = amountMoney;
		this.quest = quest;
	}
	
    public Reward(int amountMoney, Quest quest, int skillPoints) {
        this.rewardMoney = amountMoney;
        this.quest = quest;
    }
	
	public Reward(Charakter character, Quest quest) {
		this.rewardCharacter = character;
		this.quest = quest;
	}
	
	public void applyReward(Charakter character) {
	    
	    Fame fame = Jasbro.getInstance().getData().getProtagonist().getFame();
	    if (character != null && perkPoints > 0) {
	        for (int i = 0; i < perkPoints; i++) {
	            fame.modifyFame(1000);
	            character.addBonusPerk();
	        }
	    }
		GameData gameData = Jasbro.getInstance().getData();
		fame.modifyFame(rewardMoney / 10);
		if (quest != null) {
			gameData.earnMoney(rewardMoney, quest.getTitle());
		}
		else {
			gameData.earnMoney(rewardMoney, "Quest");
		}
		if (rewardCharacter != null) {
		    fame.modifyFame(1000);
            gameData.getCharacters().add(rewardCharacter);
            rewardCharacter.setOwnership(Ownership.OWNED);
		}
	}
	
	public long getPenalty() {
		if (rewardMoney != 0) {
			return rewardMoney / 4;
		}
		else if (rewardCharacter != null) {
			return rewardCharacter.calculateValue() / 4;
		}
		else {
			return 500;
		}
	}
	
	public String getRewardDescription() {
		Object arguments[] = {rewardMoney};
		if (rewardMoney != 0 && rewardCharacter == null) {
			return TextUtil.t("quest.reward.description.money", arguments);
		}
		else if (rewardMoney == 0 && rewardCharacter != null) {
			return TextUtil.t("quest.reward.description.character", rewardCharacter);
		}
		else if (rewardMoney == 0 && rewardCharacter == null) {
            return TextUtil.t("quest.reward.description.characterAndMoney", rewardCharacter, arguments);
        } 
		else if (rewardMoney != 0 && perkPoints > 0) {
            return TextUtil.t("quest.reward.description.moneyPerk", arguments);
        } 
		else {
            return "";
        }
	}
	
	public String getSuccessMessage() {
		if (rewardMoney != 0 && rewardCharacter == null) {
			return TextUtil.t("quest.reward.money", rewardMoney);
		}
		else if (rewardMoney == 0 && rewardCharacter != null) {
			return TextUtil.t("quest.reward.character", rewardCharacter);
		}
		else if (rewardMoney == 0 && rewardCharacter == null) {
			return TextUtil.t("quest.reward.characterAndMoney", rewardCharacter, rewardMoney);
		}
		else {
			return "";
		}
	}

	public int getRewardMoney() {
		return rewardMoney;
	}

	public Charakter getRewardCharacter() {
		return rewardCharacter;
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

    public int getPerkPoints() {
        return perkPoints;
    }

    public void setPerkPoints(int perkPoints) {
        this.perkPoints = perkPoints;
    }
	
	
}
