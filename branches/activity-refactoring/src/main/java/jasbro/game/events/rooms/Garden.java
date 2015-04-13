package jasbro.game.events.rooms;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.ConfigurableRoom;
import jasbro.game.housing.House;
import jasbro.game.housing.RoomType;
import jasbro.game.world.Time;

import java.util.List;

public class Garden extends ConfigurableRoom {
    private Plant plant = Plant.FLOWERS;
    private int growth = 0;

    public Garden(RoomType roomType) {
        super(roomType);
    }
    
    public Garden(RoomType roomType, House house) {
        super(roomType, house);
    }
    
    @Override
    public String getName() {
        return super.getName() + plant + " "+ growth; //TODO make prettier
    }
    
    @Override
    public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
        List<ActivityDetails> possibleActivities = super.getPossibleActivities(time, typeAmounts);
        if (growth > 50) {
            //possibleActivities.add(new ActivityDetails(ActivityType.HARVEST));
        }
        return possibleActivities;
    }

    @Override
    public void handleEvent(MyEvent e) {
        super.handleEvent(e);
        if (e.getType() == EventType.NEXTDAY) {
            if (plant == Plant.FLOWERS) {
                growth = growth + 3; //Dunnno
            }
            else {
                growth = growth + 2; //random stuff
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

    public int getGrowth() {
        return growth;
    }

    public void setGrowth(int growth) {
        this.growth = growth;
    }




    public static enum Plant {
        FLOWERS, CATNIP, WEED;
    }
}
