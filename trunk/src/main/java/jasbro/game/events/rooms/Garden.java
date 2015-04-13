package jasbro.game.events.rooms;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.ConfigurableRoom;
import jasbro.game.housing.House;
import jasbro.game.housing.RoomType;
import jasbro.game.world.Time;

import java.util.List;

public class Garden extends ConfigurableRoom {
	private Plant plant = Plant.GRASS;
	private String plantName="Grass";
	private int growth = 10;
	private int quality = 50;

	public Garden(RoomType roomType) {
		super(roomType);
	}

	public Garden(RoomType roomType, House house) {
		super(roomType, house);
	}

	@Override
	public String getName() {
		return super.getName() + "" + growth + " "+ plantName + " (Quality:" + quality +")";
	}

	@Override
	public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
		List<ActivityDetails> possibleActivities = super.getPossibleActivities(time, typeAmounts);
		possibleActivities.add(new ActivityDetails(ActivityType.HARVEST));
		return possibleActivities;
	}

	@Override
	public void handleEvent(MyEvent e) {
		super.handleEvent(e);
		if (e.getType() == EventType.NEXTDAY) {
			if(growth!=0){
				if (plant == Plant.FLOWERS) {
					growth += 1+growth /5; 
					quality = quality-2;                
				}
				else if(plant == Plant.CATNIP){
					growth += 1+growth /7; 
					quality = quality-3;   
				}
				else if(plant == Plant.CHERRIES){
					growth += 1+growth /7; 
					quality = quality-3;   
				}
				else if(plant == Plant.BERRIES){
					growth += 1+growth /7; 
					quality = quality-3;   
				}
				else if(plant == Plant.CATNIP){
					growth += 1+growth /10; 
					quality = quality-4;   
				}
				else if(plant == Plant.ORANGES){
					growth += 1+growth /10; 
					quality = quality-4;   
				}
				else if(plant == Plant.SHROOMS){
					growth += 1+growth /12; 
					quality = quality-5;   
				}
				else if(plant == Plant.VINES){
					growth += 1+growth /12; 
					quality = quality-5;   
				}
				else {
					growth += 1+growth /15; 
					quality = quality-6;   
				}
			}
			if(quality<1){
				quality=0;
				growth -= 5+growth/8;
			}
			if(quality>100 && growth!=0){
				quality=90;
				growth+=10;
			}
			if(growth>100){
				growth=100;
			}
			if(growth<0){
				growth=0;
			}
		}
	}


	public Plant getPlant() {
		return plant;
	}

	public void setPlant(Plant plant) {
		this.plant = plant;
		growth = 0;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
		
	}

	public int getGrowth() {
		return growth;
	}

	public void setGrowth(int growth) {
		this.growth = growth;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}



	public static enum Plant {
		FLOWERS, CATNIP, WEED, GRASS, CHERRIES, BERRIES, ORANGES, PEACHES, VINES, SHROOMS;
	}
}
