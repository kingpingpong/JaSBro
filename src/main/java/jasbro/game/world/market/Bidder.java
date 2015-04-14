/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.world.market;

import jasbro.Util;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 *
 * @author Azrael
 */
public abstract class Bidder extends Thread implements Serializable {
	private final static Logger log = Logger.getLogger(Bidder.class);
	public static String BIDDERNAMES[] = {"Markus Aurelius", "Lady Margaret", "Count Hensen", "Vlad", "Slave Trainer Daisy", "Vincent Adelburn", "Merchant"};
    
	private boolean stopped = false;
    private Auction auction;
    private String bidderName;
	
	public void setAuction(Auction auction) {
		this.auction = auction;
	}
	
	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}
    
    public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public Auction getAuction() {
		return auction;
	}

    public void stopBidding() {
        stopped = true;
    }


	public static class LimitBidder extends Bidder {
        private int maxValue;


		public LimitBidder(String bidderName) {
            setBidderName(bidderName);
        }

        @Override
        public void run() {
        	try {
	        	setStopped(false);
	            while (!isStopped()) {
	                try {
	                    Thread.sleep(Util.getInt(2000, 12000));
	                }
	                catch (Exception e) { 
	                	e.printStackTrace();
	                }
	                long maxBid = getAuction().getMaxBid();
	                if (maxBid < getMaxValue() && getAuction().getMaxBidder() != this) {
	                	getAuction().bid(maxBid+Util.getInt(1, 15) + getAuction().getSlaveValue() / 20, this);
	                }
	            }
        	}
        	catch (Exception e) {
        		log.error("Error in bidder", e);
        	}            
        }

		public int getMaxValue() {
			if (maxValue == 0 && getAuction() != null) {
				int percent = Util.getRnd().nextInt(21)+70;
	            maxValue = (int)(getAuction().getSlave().calculateValue() * percent / 100.0);
			}
			return maxValue;
		}        
    }
	
	public static class MinimumBidder extends Bidder {
		public MinimumBidder() {
			setBidderName("Treasurer Cortez");
		}
		
		@Override
		public void run() {
			setStopped(false);
            while (!isStopped()) {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) { 
                	e.printStackTrace();
                }
                long maxBid = getAuction().getMaxBid();
                if (getAuction().getMaxBidder() != this && maxBid < getAuction().getSlaveValue()) {
                	getAuction().bid(maxBid+1, this);
                }
            }
		}
	}
	
	public static class ShockBidder extends Bidder {
		public ShockBidder() {
			setBidderName("Lord Baratheon");
		}
		
		@Override
		public void run() {
			try {
	        	setStopped(false);
	            while (!isStopped()) {
	                try {
	                    Thread.sleep(Util.getInt(4000, 12000));
	                }
	                catch (Exception e) { 
	                	e.printStackTrace();
	                }
	                long maxBid = getAuction().getMaxBid();
	                if (getAuction().getMaxBidder() != this &&
	                		maxBid < getAuction().getSlaveValue() * 2) {
	                	long bid = (maxBid * 2) ;
            			if (bid < 1000) {
            				bid = bid / 10 * 10;
            			}
            			else {
            				bid = bid / 100 * 100;
            			}
	                	getAuction().bid(bid, this);
	                }
	                try {
	                    Thread.sleep(10000);
	                }
	                catch (Exception e) { 
	                	e.printStackTrace();
	                }
	            }
        	}
        	catch (Exception e) {
        		log.error("Error in bidder", e);
        	}
		}
	}
	
	public static class PersistentBidder extends Bidder {
		private long lastBid = 0;
		public PersistentBidder() {
			setBidderName("Prince Joffrick");
		}
		
		@Override
		public void run() {
			try {
	        	setStopped(false);
	            while (!isStopped()) {
	                try {
	                    Thread.sleep(Util.getInt(4000, 6000));
	                }
	                catch (Exception e) { 
	                	e.printStackTrace();
	                }
	                long maxBid = getAuction().getMaxBid();
	                if (lastBid == 0 || (getAuction().getMaxBidder() != this && maxBid - lastBid < getAuction().getSlaveValue() / 3
	                		&& maxBid < getAuction().getSlaveValue() * 2)) {
	                	long bid = maxBid + maxBid / 10;
	                	getAuction().bid(bid, this);
	                	lastBid = bid;
	                }
	                else if (getAuction().getMaxBidder() != this) {
	                	getAuction().addMsg(getBidderName() + " sighs and resigns.");
	                	setStopped(true);
	                }
	            }
        	}
        	catch (Exception e) {
        		log.error("Error in bidder", e);
        	}
		}
	}
	
	public static class UiBidder extends Bidder {
		public UiBidder() {
			setBidderName("You");
		}
		
		public void bid(long amount) {
			getAuction().bid(amount, this);
		}
	}
}
