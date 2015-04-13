package jasbro.game.character.activities.sub;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.game.world.customContent.npc.ComplexEnemy;
import jasbro.game.world.customContent.npc.EnemySpawnLocation;
import jasbro.game.world.locations.LocationType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Explore extends RunningActivity {
    public List<ComplexEnemy> defeatedEnemies = new ArrayList<ComplexEnemy>();    
	
	@Override
	public void init() {
	}
	
    @Override
    public MessageData getBaseMessage() {
    	String message = TextUtil.t("explore.basic", getCharacter());
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter());
        MessageData messageData = new MessageData(message, image, getCharacter().getBackground());        
        return messageData;
    }
    
    @Override
    public List<ModificationData> getStatModifications() {    	
    	List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();   
        modifications.add(new ModificationData(TargetType.ALL, -30, EssentialAttributes.ENERGY));
    	return modifications;
    }
    
    @Override
    public void perform() {
        
        /*
         * if (enemy.getCharacterBaseId() != null && Jasbro.getInstance().getCharacterBases().contains(enemy.getCharacterBaseId())) {
                //TODO capture enemy
            }
         */
    }
    
    public EnemySpawnLocation getEnemySpawnLocation() {
        if (getPlannedActivity().getSource().getLocationType() == LocationType.DUNGEON1) {
            return EnemySpawnLocation.DUNGEON1;
        }
        else if (getPlannedActivity().getSource().getLocationType() == LocationType.DUNGEON2) {
            return EnemySpawnLocation.DUNGEON2;
        }
        else if (getPlannedActivity().getSource().getLocationType() == LocationType.DUNGEON3) {
            return EnemySpawnLocation.DUNGEON3;
        }
        else {
            return EnemySpawnLocation.DUNGEON4;
        }
    }

    public List<ComplexEnemy> getDefeatedEnemies() {
        return defeatedEnemies;
    }

    public void setDefeatedEnemies(List<ComplexEnemy> defeatedEnemies) {
        this.defeatedEnemies = defeatedEnemies;
    }
    
}
