package jasbro.stats;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MoneyChangedEvent;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.Customer.SatisfactionModifier;
import jasbro.game.housing.House;
import jasbro.game.world.Time;
import jasbro.gui.pages.StatScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatCollector implements CentralEventlistener {	
	private transient StatScreen statScreen;
	private transient Map<Time, ShiftData> shiftDataMap = new HashMap<Time, ShiftData>();
	private transient ShiftData dailyData;
	private transient ShiftData previousDayData;

	public StatCollector() {
		reset();
	}
	
	@Override
	public void handleCentralEvent(MyEvent e) {
		Time shift = Jasbro.getInstance().getData().getTime();
		if (shiftDataMap.get(shift).isClosed() && (e.getType() == EventType.ACTIVITYPERFORMED ||
				e.getType() == EventType.MONEYEARNED || e.getType() == EventType.MONEYSPENT)) {
			reset();
		}
		if (e.getType() == EventType.SHIFTSTART) {
			initMoneyBeforeShift();
		}
		dailyData.recordEvent(e);
		shiftDataMap.get(shift).recordEvent(e);
	};


	public void showStatScreen() {
		showStatScreen(null, true);
	}
	
	public void showStatScreen(Time time, boolean important) {
		statScreen = new StatScreen(this, time, important);
		Jasbro.getInstance().getGui().addMessage(statScreen);
	}

	public void reset() {
		previousDayData = dailyData;
		dailyData = new ShiftData();
		shiftDataMap.clear();
		for (Time time : Time.values()) {
			shiftDataMap.put(time, new ShiftData());
		}
		Time shift = Jasbro.getInstance().getData().getTime();
		if (shift != Time.MORNING) {
			shiftDataMap.get(Time.MORNING).setMoneyAfterShift(0);;
		}
	}
	

	private void initMoneyBeforeShift() {
		Time shift = Jasbro.getInstance().getData().getTime();
		dailyData.initMoneyBeforeShift();
		shiftDataMap.get(shift).initMoneyBeforeShift();
	}


	public void setMoneyAfterShift(long money) {
		Time shift = Jasbro.getInstance().getData().getTime().getPreviousTimeOfDay();
		dailyData.setMoneyAfterShift(money);
		shiftDataMap.get(shift).setMoneyAfterShift(money);
	}
	
	public ShiftData getShiftDataMap(Time time) {
		return shiftDataMap.get(time);
	}


	public ShiftData getDailyData() {
		return dailyData;
	}
	
	public ShiftData getPreviousDayData() {
		return previousDayData;
	}



	public class ShiftData {
		private Long moneyBeforeShift = null;
		private Long moneyAfterShift;
		private List<ActivityData> activityDataList = new ArrayList<ActivityData>();
		private List<MoneyChangeData> moneyEarnedList = new ArrayList<MoneyChangeData>();
		private List<MoneyChangeData> moneySpentList = new ArrayList<MoneyChangeData>();
		
		public void recordEvent(MyEvent e) {
			if (e.getType() == EventType.ACTIVITYPERFORMED) {
				initMoneyBeforeShift();
				RunningActivity activity = (RunningActivity) e.getSource();
				
				ActivityData activityData = new ActivityData(activity, this);
				if (!activityDataList.contains(activityData)) {
					if (activityData.getType() != ActivityType.WHORESTREETS && activityData.getType() != ActivityType.EVENT) {
						activityDataList.add(activityData);
					}
				}
				else {
					ActivityData origActivityData = activityDataList.get(activityDataList.indexOf(activityData));
					origActivityData.addToIncome(activityData.getIncome());
					origActivityData.amountCustomers += activityData.amountCustomers;
					origActivityData.amountMainCustomers += activityData.amountMainCustomers;
					origActivityData.satisfactionModifier = origActivityData.satisfactionModifier + activityData.satisfactionModifier;
					origActivityData.amountModifiers++;
					origActivityData.satisfactionMainCustomer = origActivityData.satisfactionMainCustomer + activityData.satisfactionMainCustomer;
				}
			}
			else if (e.getType() == EventType.MONEYEARNED) {
				initMoneyBeforeShift();
				MoneyChangeData moneyChangeData = new MoneyChangeData(e);			
				if (!moneyEarnedList.contains(moneyChangeData)) {
					moneyEarnedList.add(moneyChangeData);
				}
				else {
					MoneyChangeData existingMoneyChangeData = moneyEarnedList.get(moneyEarnedList.indexOf(moneyChangeData));
					existingMoneyChangeData.addToValue(moneyChangeData.getAmount());
				}
			}
			else if (e.getType() == EventType.MONEYSPENT) {
				initMoneyBeforeShift();
				MoneyChangeData moneyChangeData = new MoneyChangeData(e);
				if (!moneySpentList.contains(moneyChangeData)) {
					moneySpentList.add(moneyChangeData);
				}
				else {
					MoneyChangeData existingMoneyChangeData = moneySpentList.get(moneySpentList.indexOf(moneyChangeData));
					existingMoneyChangeData.addToValue(moneyChangeData.getAmount());
				}
			}
		}
		
		public void initMoneyBeforeShift(MyEvent event) {
			if (moneyBeforeShift == null) {
				moneyBeforeShift = Jasbro.getInstance().getData().getMoney();
			}
		}
		
		public void initMoneyBeforeShift() {
			if (moneyBeforeShift == null) {
				moneyBeforeShift = Jasbro.getInstance().getData().getMoney();
			}
		}

		public long getMoneyBeforeShift() {
			if (moneyBeforeShift == null) {
				return 0;
			}
			return moneyBeforeShift;
		}

		public long getMoneyAfterShift() {
			if (moneyAfterShift == null) {
				return Jasbro.getInstance().getData().getMoney();
			}
			return moneyAfterShift;
		}

		public void setMoneyAfterShift(long moneyAfterShift) {
			this.moneyAfterShift = moneyAfterShift;
		}

		public List<ActivityData> getActivityDataList() {
			if (activityDataList == null) {
				activityDataList = new ArrayList<ActivityData>();
			}
			
			Comparator<ActivityData> comparator = new Comparator<StatCollector.ActivityData>() {
				@Override
				public int compare(ActivityData o1, ActivityData o2) {
					return ((Integer) o1.getIncome()).compareTo(o2.getIncome());
				}
			};
			Collections.sort(activityDataList, Collections.reverseOrder(comparator));
			return activityDataList;
		}
		
		public int getAverageSatisfactionModifier(RunningActivity activity) {
			List<Customer> customers = new ArrayList<Customer>();
			customers.addAll(activity.getCustomers());
			if (customers.size() == 0) {
				return 0;
			}
			int sum = 0;
			for (Customer customer : customers) {
				for (SatisfactionModifier satisfactionModifier : customer.getSatisfactionModifiers()) {
					if (satisfactionModifier.getSource() == activity) {
						sum += satisfactionModifier.getModifiedBy();
					}
				}
			}
			return sum / customers.size();
		}
		
		public int getAverageFinalSatisfaction(RunningActivity activity) {
			List<Customer> customers = new ArrayList<Customer>();
			customers.addAll(activity.getMainCustomers());
			if (customers.size() == 0) {
				return 0;
			}
			int sum = 0;
			for (Customer customer : customers) {
				sum += customer.getSatisfactionAmount();
			}
			return sum / customers.size();
		}
		
		public long calculateSum(List<MoneyChangeData> moneyChangeDataList) {
			int sum = 0;
			for (MoneyChangeData moneyChangeData : moneyChangeDataList) {
				sum += moneyChangeData.getAmount();
			}
			return sum;
		}

	    public List<MoneyChangeData> getMoneyEarnedList() {
	    	if (moneyEarnedList == null) {
	    		moneyEarnedList = new ArrayList<MoneyChangeData>();
	    	}
	    	Collections.sort(moneyEarnedList);
	    	Collections.reverse(moneyEarnedList);
			return moneyEarnedList;
		}

		public List<MoneyChangeData> getMoneySpentList() {
	    	if (moneySpentList == null) {
	    		moneySpentList = new ArrayList<MoneyChangeData>();
	    	}
	    	Collections.sort(moneySpentList);
	    	Collections.reverse(moneySpentList);
			return moneySpentList;
		}

		public boolean isClosed() {
			return moneyAfterShift != null;
		}
		
		public boolean isInitialized() {
			return moneyBeforeShift != null;
		}
	}


	public class ActivityData {
    	private ActivityType type;
    	private List<Charakter> characters;
    	private int income;
    	private int amountCustomers;
    	private int satisfactionModifier;
    	private int amountModifiers;
    	private House house;
    	private int satisfactionMainCustomer;
    	private int amountMainCustomers;
    	
		public ActivityData(RunningActivity activity, ShiftData currentShift) {
			super();
			this.type = activity.getType();
			this.characters = activity.getCharacters();
			this.income = activity.getIncome();
			this.amountCustomers = activity.getCustomers().size();
			this.satisfactionModifier = currentShift.getAverageSatisfactionModifier(activity);
			this.amountModifiers = 1;
			this.house = activity.getHouse();
			this.amountMainCustomers = activity.getMainCustomers().size();
			this.satisfactionMainCustomer = currentShift.getAverageFinalSatisfaction(activity);
		}
		
		public void addToIncome(int amount) {
			income += amount;
		}
		
		public ActivityType getType() {
			return type;
		}
		public void setType(ActivityType type) {
			this.type = type;
		}
		public List<Charakter> getCharacters() {
			return characters;
		}
		public void setCharacters(List<Charakter> characters) {
			this.characters = characters;
		}
		public int getIncome() {
			return income;
		}
		public void setIncome(int income) {
			this.income = income;
		}
		public int getAmountCustomers() {
			return amountCustomers;
		}
		public void setAmountCustomers(int amountCustomers) {
			this.amountCustomers = amountCustomers;
		}

		public int getAvgSatisfactionModifier() {
			return satisfactionModifier / amountModifiers;
		}

		public House getHouse() {
			return house;
		}

		public int getAvgSatisfactionMainCustomer() {
			if (amountMainCustomers == 0) {
				return 0;
			}
			return satisfactionMainCustomer / amountMainCustomers;
		}

		public int getAmountMainCustomers() {
			return amountMainCustomers;
		}

		public void setAmountMainCustomers(int amountMainCustomers) {
			this.amountMainCustomers = amountMainCustomers;
		}

		public void setHouse(House house) {
			this.house = house;
		}

		public void setSatisfactionMainCustomer(int satisfactionMainCustomer) {
			this.satisfactionMainCustomer = satisfactionMainCustomer;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((characters == null) ? 0 : characters.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ActivityData other = (ActivityData) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (characters == null) {
				if (other.characters != null)
					return false;
			} else if (!characters.equals(other.characters))
				return false;
			if (type != other.type)
				return false;
			return true;
		}

		private StatCollector getOuterType() {
			return StatCollector.this;
		}
    }
    
    public class MoneyChangeData implements Comparable<MoneyChangeData> {
    	private String source;
    	private long amount;
    	
		public MoneyChangeData(String source, long amount) {
			super();
			this.source = source;
			this.amount = amount;
		}
		
		public void addToValue(long amount) {
			this.amount += amount;
		}

		public MoneyChangeData(MyEvent event) {
			super();
			MoneyChangedEvent moneyChangedEvent = (MoneyChangedEvent) event;
			amount = moneyChangedEvent.getAmount();
			source = moneyChangedEvent.getSource().toString();
		}	
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MoneyChangeData other = (MoneyChangeData) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}

		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public long getAmount() {
			return amount;
		}
		public void setAmount(long amount) {
			this.amount = amount;
		}

		private StatCollector getOuterType() {
			return StatCollector.this;
		}

		@Override
		public int compareTo(MoneyChangeData o) {
			if (o == null) {
				return 0;
			}
			return ((Long)amount).compareTo(o.getAmount());
		}
    }
}
