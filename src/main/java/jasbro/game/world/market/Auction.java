/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.market.Bidder.LimitBidder;
import jasbro.game.world.market.Bidder.MinimumBidder;
import jasbro.game.world.market.Bidder.PersistentBidder;
import jasbro.game.world.market.Bidder.ShockBidder;
import jasbro.game.world.market.Bidder.UiBidder;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 *
 * @author Azrael
 */
public class Auction extends TimerTask {
	private final static Logger log = Logger.getLogger(Auction.class);
    private long maxBid = 0;
    private Bidder maxBidder;
    private Charakter slave;
    private Long slaveValue;
    private AuctionGui gui;
    private boolean ownSlave = false;

    private List<Bidder> bidders;
    private Timer timer = new Timer();
    private final static int soldSeconds = 10; 
    private int auctionTime = 0;
    private int remainingTime = soldSeconds;
    private String messages = "";
    private int profit;
    private boolean abort = false;

    public void startAuction() {
    	GameData gameData = Jasbro.getInstance().getData();
    	if (gameData.getCharacters().contains(slave)) {
    		ownSlave = true;
    	}
    	
    	printBidderParticipants(getBidders());
    	addMsg("The auction of " + slave.getName() + " starts!");
        for (Bidder bidder : getBidders()) {
        	try {
        	bidder.setAuction(this);
            if (bidder instanceof Thread) {
                ((Thread)bidder).start();
            }
        	}catch (Exception e) {
        		log.error("Bidder not started", e);
        	}
        }
        timer.scheduleAtFixedRate(this, 1000, 1000);
    }

    private void printBidderParticipants(List<Bidder> bidders) {
    	String msg = "The other bidders are: ";
		for (int i = 0; i < bidders.size(); i++) {
			msg += bidders.get(i).getBidderName();			
			if (i < bidders.size()-1) {
				if (i == bidders.size()-2) {
					msg += " and ";
				}
				else {
					msg += ", ";
				}
			}
		}
		msg += ".";
		addMsg(msg);				
	}

	public synchronized boolean bid(long amount, Bidder bidder) {
		if	(bidder instanceof UiBidder && !Jasbro.getInstance().getData().canAfford(amount)) {
			addMsg("You can not affort to bid " + amount + " gold");
			return false;
		}
        if (remainingTime > 0 && amount > maxBid) {
            this.maxBid = amount;
            maxBidder = bidder;
            resetTimer();
            if (bidder instanceof UiBidder) {
            	Object arguments[] = {amount};
                addMsg(TextUtil.t("auction.bid", arguments));
            }
            else {
            	Object arguments[] = {bidder.getBidderName(), amount};
                addMsg(TextUtil.t("auction.bidCompetitor", arguments));
            }
            return true;
        }
        else {
            return false;
        }
    }

    private void resetTimer() {
		remainingTime = soldSeconds;
		remainingTime -= auctionTime / 20;
		if (remainingTime < 5) {
			remainingTime = 5;
		}
	}

	public synchronized long getMaxBid() {
        return maxBid;
    }

    public synchronized Bidder getMaxBidder() {
        return maxBidder;
    }

	public Charakter getSlave() {
		return slave;
	}

	public void setSlave(Charakter slave) {
		this.slave = slave;
	}

	@Override
	public synchronized void run() {
		auctionTime++;
		if (!abort) {
		    if (maxBid > 0) {
	            remainingTime--;
	        }
	        if (remainingTime == 4) {
	            addMsg("Going once.");
	        }
	        else if (remainingTime == 2) {
	            addMsg("Going twice.");
	        }
	        else if (remainingTime <= 0) {
	            timer.cancel();
	            addMsg("Sold!");
	            for (Bidder bidder : getBidders()) {
	                bidder.stopBidding();
	            }
	            try {
	                Thread.sleep(2000);
	            } catch (InterruptedException e) {
	                log.error("InterruptedException", e);
	            }
	            
	            GameData gameData = Jasbro.getInstance().getData();
	            profit = 0;
	            if (ownSlave) {
	                profit = (int) Util.getPercent(maxBid, 80);
	                Object arguments[] = {slave.getName()};
	                Jasbro.getInstance().getData().earnMoney(profit, TextUtil.t("auction.stats", arguments));
	            }
	            if (maxBidder instanceof UiBidder) {
	                Object arguments[] = {slave.getName()};
	                gameData.spendMoney(maxBid, TextUtil.t("auction.stats", arguments));
	                if (!ownSlave) {
	                    gameData.getCharacters().add(slave);
	                    slave.setOwnership(Ownership.OWNED);
	                    new MessageScreen("Congratulations! You bought " + slave.getName() + " for " + maxBid + " gold.", 
	                            ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, slave), slave.getBackground());
	                    gameData.getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, slave));
	                }
	                else {
	                    new MessageScreen("You bought back your own slave! Due to commission and taxes you lose " 
	                            + (maxBid - profit) + " gold.", 
	                            ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, slave), slave.getBackground());
	                }
	            }
	            else {
	                if (!ownSlave) {
	                    new MessageScreen(maxBidder.getBidderName() +" bought " + slave.getName() + " for " + maxBid + " gold.", 
	                            ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, slave), slave.getBackground());
	                }
	                else {
	                    new MessageScreen(TextUtil.t("auction.soldOwned", slave, new Object[] {maxBidder.getBidderName(), profit}), 
	                            ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, slave), slave.getBackground());
	                    MyEvent event = new MyEvent(EventType.SLAVESOLD, this);
	                    Jasbro.getInstance().getData().getEventManager().handleEvent(event);
	                    Jasbro.getInstance().removeCharacter(slave);
	                    
	                    double bonusFame = Math.round(Math.sqrt(profit) * 10 - 500);
	                    Jasbro.getInstance().getData().getProtagonist().getFame().modifyFame(bonusFame);
	                    //TODO add comment based on the fame gained
	                }
	            }
	            gui.close();
	        }
		}
		else {
            timer.cancel();
            for (Bidder bidder : getBidders()) {
                bidder.stopBidding();
            }
            gui.close();
		}
		
	}
	
	public void addMsg(String msg) {
		messages += msg + "\n";
		gui.update();
	}
	
	public String getMessages() {
		return messages;
	}

	public int getAuctionTime() {
		return auctionTime;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setBidders(List<Bidder> bidders) {
		this.bidders = bidders;
	}
	
	public AuctionGui getGui() {
		return gui;
	}

	public void setGui(AuctionGui gui) {
		this.gui = gui;
	}
	
	public List<Bidder> getBidders() {
		if (bidders == null) {
			bidders = new ArrayList<Bidder>();
			int amount = Util.getInt(3, 6);
			for (int i = 0; i < amount; i++) {
				bidders.add(createBidder(bidders));
			}
		}
		return bidders;
	}
	
	public Bidder createBidder(List<Bidder> bidders) {
		if (!contains(bidders, MinimumBidder.class)) {
			if (Util.getRnd().nextInt(100) < 5) {
				return new MinimumBidder();
			}
		}
		if (!contains(bidders, ShockBidder.class)) {
			if (Util.getRnd().nextInt(100) < 5) {
				return new ShockBidder();
			}
		}
		if (!contains(bidders, PersistentBidder.class)) {
			if (Util.getRnd().nextInt(100) < 5) {
				return new PersistentBidder();
			}
		}
		
		String name;
		do {
			name = Bidder.BIDDERNAMES[Util.getRnd().nextInt(Bidder.BIDDERNAMES.length)];
		}
		while(containsName(bidders, name));
    	return new LimitBidder(name);    	
    }

	@SuppressWarnings("rawtypes")
	private boolean contains(List<Bidder> bidders, Class checkClass) {
		for (Bidder bidder : bidders) {
			if (checkClass.isInstance(bidder)) {
				return true;
			}
		}
		return false;
	}

	private boolean containsName(List<Bidder> bidders, String name) {
		for (Bidder bidder : bidders) {
			if (name.equals(bidder.getBidderName())) {
				return true;
			}
		}
		return false;
	}

	public long getSlaveValue() {
		if (slaveValue == null && slave != null) {
			slaveValue = slave.calculateValue();
		}
		return slaveValue;
	}

	public int getProfit() {
		return profit;
	}

	public static interface AuctionGui {
		public void update();
		public void close();
	}

    public boolean isAbort() {
        return abort;
    }

    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    public boolean isOwnSlave() {
        return ownSlave;
    }
}
