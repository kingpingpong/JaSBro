package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.conditions.Questtimer;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.Fame;
import jasbro.game.events.business.SpawnData;
import jasbro.game.world.market.Auction;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class SellSlaveQuest extends Quest {

    private Charakter slave;
    private int timeRemaining;
    private int targetAmount;
    private String clientType;
    
	public SellSlaveQuest(Charakter slave, int time, int targetAmount) {
	    this.slave = slave;
	    this.timeRemaining = time;
	    this.targetAmount = targetAmount;
	}

    @Override
    public List<QuestStage> getInitStages() {
        List<QuestStage> questStages = new ArrayList<QuestStage>();
        questStages.add(new SellGirlQuestStage());
        return questStages;
    }
	
	private class SellGirlQuestStage extends QuestStage {
		
		@Override
		public void init(Quest quest) {
			String text = TextUtil.t("quest.startet") + " " + getTitle(quest) + "\n" + getDescription(quest);
			new MessageScreen(text, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, slave), slave.getBackground());
			Jasbro.getInstance().getData().getCharacters().add(getSlave());
			getSlave().getConditions().add(new Questtimer(getTimeRemaining(), SellSlaveQuest.this));
		}
		
		@Override
		public String getTitle(Quest quest) {
			return TextUtil.t("quests.sellgirlquest.title", getSlave());
		}
		
		@Override
		public String getDescription(Quest quest) {
			String arguments[] = {getClientType()};
			String text = TextUtil.t("quests.sellgirlquest.description", getSlave(), arguments) + "\n";
			String argumentsTime[] = {getTimeRemaining() + ""};
			text +=	TextUtil.t("quest.time", argumentsTime) + "\n";
			String argumentsAmount[] = {targetAmount+""};
			text += TextUtil.t("quests.sellgirlquest.targetamount", getSlave(), argumentsAmount) + "\n";
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
				gameData.spendMoney(cost, SellSlaveQuest.this);
				gameData.getProtagonist().getFame().modifyFame(-100);
				String arguments[] = {""+cost};
				new MessageScreen(TextUtil.t("quests.standardgirlquest.death", arguments), 
						new ImageData("images/backgrounds/coffin.jpg"), null, true);
			}
			else if (e.getType() == EventType.NEXTDAY) {
				timeRemaining--;
				if (getTimeRemaining() <= 0) {
					Jasbro.getInstance().removeCharacter(slave);
					gameData.spendMoney(500, SellSlaveQuest.this);
					String arguments[] = {500+""};
					new MessageScreen(TextUtil.t("quests.sellgirlquest.timeup", getSlave(), arguments), 
							ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getSlave()), getSlave().getBackground(), true);
					Jasbro.getInstance().getData().getQuestManager().setSolved(SellSlaveQuest.this, -100);
				}
			}
			else if (e.getType() == EventType.SLAVESOLD && e.getSource() instanceof Auction) {
				Auction auction = (Auction) e.getSource();
				if (auction.getSlave() == slave) {
					if (auction.getMaxBid() >= targetAmount) {
						int reward = auction.getProfit() / 2;
						String arguments[] = {reward+""};
						new MessageScreen(TextUtil.t("quests.sellgirlquest.success", getSlave(), arguments), 
								ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getSlave()), 
								getSlave().getBackground(), true);
						gameData.spendMoney(auction.getProfit() - reward, SellSlaveQuest.this);
						gameData.getProtagonist().getFame().modifyFame(reward / 5);
						Jasbro.getInstance().getData().getQuestManager().setSolved(SellSlaveQuest.this, 10);
					}
					else {
						int penalty = 500;
						gameData.getProtagonist().getFame().modifyFame(-500);
						String arguments[] = {auction.getProfit()+"", penalty+""};
						new MessageScreen(TextUtil.t("quests.sellgirlquest.failed", getSlave(), arguments), 
								ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getSlave()), 
								getSlave().getBackground(), true);
						gameData.spendMoney(auction.getProfit() + penalty, SellSlaveQuest.this);
						Jasbro.getInstance().getData().getQuestManager().setSolved(SellSlaveQuest.this, -5);
					}
				}
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
	}
	
	public static SellSlaveQuest generate(int difficultyModifier) {
		Charakter character = Jasbro.getInstance().generateBasicSlave();
		character.setOwnership(Ownership.NOTOWNEDCANSELL);
		int targetAmount = 900 + (difficultyModifier / 20) * 200;
		int time = 30;
		return new SellSlaveQuest(character, time, targetAmount);
	}
}
