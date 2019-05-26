package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.rooms.Garden;
import jasbro.game.events.rooms.Garden.Plant;
import jasbro.game.items.Item;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Harvest extends RunningActivity {
	
	private final static float BASEMODIFICATION = 1f;
	private final static float OBEDIENCEMODIFICATION = 0.01f;
	private int efficiency = 1;
	private int amount=0;
	
	private final static String daisies = "Daisies";
	private final static String tulips = "Tulips";
	private final static String roses = "Roses";
	private final static String orchids = "Orchids";
	
	private final static String catnip = "Catnip";
	private final static String strongCatnip = "Strong Catnip";
	
	private final static String weed = "Weed";
	private final static String hqWeed = "High Quality Weed";
	
	private final static String peach = "Peach";
	private final static String hqpeach = "RoundPeach";
	
	private final static String orange = "Orange";
	private final static String hqorange = "AnnoyingOrange";
	
	private final static String cherry = "Cherry";
	private final static String hqcherry = "Cherrypop";
	
	private final static String berry = "Berries";
	private final static String hqberry = "Plant_Cumberry";
	
	private final static String shroom = "Mushroom";
	private final static String hqshroom = "Plant_Penishroom";
	
	private final static String vine = "Vines";
	private final static String hqvine = "Plant_TentacleVine";
	
	
	
	
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		float modification = BASEMODIFICATION;
		
		modifications.add(new ModificationData(TargetType.SLAVE, OBEDIENCEMODIFICATION, BaseAttributeTypes.OBEDIENCE));
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		return modifications;
	}
	
	@Override
	public void init() {
		efficiency+=getCharacters().get(0).getIntelligence();
	}
	
	@Override
	public void perform() {
		
		
		
	}
	
	@Override
	public MessageData getBaseMessage() {
		Charakter character = getCharacters().get(0);
		ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character);
		Garden room = (Garden)getCharacterLocation();
		Item item = Jasbro.getInstance().getItems().get(daisies);
		String itemString ="";
		if(room.getPlant()==Plant.FLOWERS){
			if(room.getQuality()<30){
				amount=room.getGrowth()/5;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(daisies);
				itemString="bouquets of daisies";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
				
			}
			else if(room.getQuality()<60){
				amount=room.getGrowth()/10;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(tulips);
				itemString="bouquets of tulips";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			else if(room.getQuality()<90){
				amount=room.getGrowth()/15;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(roses);
				itemString="bouquets of roses";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			else{
				amount=room.getGrowth()/20;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(orchids);
				itemString="bouquets of orchids";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		if(room.getPlant()==Plant.CATNIP){
			if(room.getQuality()<50){
				amount=room.getGrowth()/7;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(catnip);
				itemString="boxes of catnip";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			
			else{
				amount=room.getGrowth()/14;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(strongCatnip);
				itemString="boxes of strong catnip";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		if(room.getPlant()==Plant.WEED){
			if(room.getQuality()<50){
				amount=room.getGrowth()/20;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(weed);
				itemString="patches of weed";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			
			else{
				amount=room.getGrowth()/30;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(hqWeed);
				itemString="patchers of high quality weed";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		if(room.getPlant()==Plant.CHERRIES){
			if(room.getQuality()<50){
				amount=room.getGrowth()/16;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(cherry);
				itemString="box of cherries";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			
			else{
				amount=room.getGrowth()/18;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(hqcherry);
				itemString="boxes of cherrypops";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		if(room.getPlant()==Plant.BERRIES){
			if(room.getQuality()<50){
				amount=room.getGrowth()/15;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(berry);
				itemString="bags of berries";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			
			else{
				amount=room.getGrowth()/20;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(hqberry);
				itemString="bags of cumberries";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		if(room.getPlant()==Plant.ORANGES){
			if(room.getQuality()<50){
				amount=room.getGrowth()/5;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(orange);
				itemString="oranges";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			
			else{
				amount=room.getGrowth()/15;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(hqorange);
				itemString="annoying oranges";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		if(room.getPlant()==Plant.PEACHES){
			if(room.getQuality()<50){
				amount=room.getGrowth()/18;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(peach);
				itemString="peaches";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			
			else{
				amount=room.getGrowth()/24;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(hqpeach);
				itemString="deliciously round peaches.";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		if(room.getPlant()==Plant.VINES){
			if(room.getQuality()<50){
				amount=room.getGrowth()/30;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(vine);
				itemString="bundles of weird vines.";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			
			else{
				amount=room.getGrowth()/40;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(hqvine);
				itemString="wiggly bundles of tentacle vines";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		if(room.getPlant()==Plant.SHROOMS){
			if(room.getQuality()<50){
				amount=room.getGrowth()/20;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(shroom);
				itemString="shrooms";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
			
			else{
				amount=room.getGrowth()/30;
				if(character.getTraits().contains(Trait.FLOWERARRANGEMENT)){amount*=2;}
				item = Jasbro.getInstance().getItems().get(hqshroom);
				itemString="penishrooms";
				Jasbro.getInstance().getData().getInventory().addItems(item, amount);
			}
		}
		
		Object arguments[] = {amount, itemString};
		String message ="";
		if(amount>0 && room.getPlant()!=Plant.GRASS){ message = TextUtil.t("harvest.basic", character,arguments);}
		else if(room.getPlant()==Plant.GRASS){ message = TextUtil.t("harvest.grass", character);}
		else{message = TextUtil.t("harvest.none", character,arguments);}
		room.setGrowth(0);
		room.setPlant(Plant.GRASS);
		 room.setPlantName("Grass");
		return new MessageData(message, image, null);
	}
	
	
	
	public float getEfficiency() {
		return efficiency;
	}
	
	public void setEfficiencyn(int efficiency) {
		this.efficiency = efficiency;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}