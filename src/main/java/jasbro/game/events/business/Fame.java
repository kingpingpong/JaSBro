package jasbro.game.events.business;

import jasbro.texts.TextUtil;

import java.io.Serializable;

public class Fame implements Serializable {
	private double fame = 0;
	
	public void modifyFame(double fameModifier) {
		fame += fameModifier;
		
		if (fame < 0) {
			fame = 0;
		}
	}
	
	public long getFame() {
		return (long)fame;
	}
	
	public FameState getFameCharacter() {
		return FameState.getFameStateCharacter(getFame());
	}
	
	public FameState getFameBuilding() {
		return FameState.getFameStateBuilding(getFame());
	}
	
	public static enum FameState {
		OBSCURE(-1000, -1000), 
		UNKNOWN(5, 50), 
		UNFAMILIAR(50, 500), 
		UNREMARKABLE(200, 2000), 
		NOTED(1000, 20000), 
		RUMORED(5000, 100000), 
		REPUTABLE(20000, 500000), 
		RENOWNED(50000, 1000000), 
		INFLUENTIAL(100000, 5000000), 
		ACCLAIMED(200000, 10000000), 
		EMINENT(300000, 50000000), 
		CELEBRIOUS(500000, 100000000), 
		LEGENDARY(1000000, 1000000000);
		
		private long minFameCharacter;
		private long minFameBuilding;
		
		private FameState(long minFameCharacter, long minFameBuilding) {
			this.minFameCharacter = minFameCharacter;
			this.minFameBuilding = minFameBuilding;
		}
		
		public long getMinFameCharacter() {
			return minFameCharacter;
		}
		
		public long getMinFameBuilding() {
			return minFameBuilding;
		}
		
		public String getText() {
			String text = TextUtil.tNoCheck("fame."+this.toString());
			if (text == null) {
				text = this.toString();
				text = text.charAt(0) + text.substring(1).toLowerCase();
			}
			return text;
		}
		
		public static FameState getFameStateCharacter(long fame) {
			FameState previousValue = null;
			for (FameState satisfaction : FameState.values()) {
				if (previousValue != null && satisfaction.getMinFameCharacter() > fame) {
					return previousValue;
				}
				previousValue = satisfaction;
			}
			return LEGENDARY;
		}
		
		public static FameState getFameStateBuilding(long fame) {
			FameState previousValue = null;
			for (FameState satisfaction : FameState.values()) {
				if (previousValue != null && satisfaction.getMinFameBuilding() > fame) {
					return previousValue;
				}
				previousValue = satisfaction;
			}
			return LEGENDARY;
		}
	}
}