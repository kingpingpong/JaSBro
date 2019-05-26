package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.rooms.Garden;
import jasbro.game.events.rooms.Garden.Plant;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Gardening extends RunningActivity {
	
	private final static float OBEDIENCEMODIFICATION = 0.01f;
	private int efficiency = 3;

	
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		
		
		modifications.add(new ModificationData(TargetType.SLAVE, OBEDIENCEMODIFICATION, BaseAttributeTypes.OBEDIENCE));
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 1.0f, SpecializationAttribute.PLANTKNOWLEDGE));
		if(getCharacters().get(0).getTraits().contains(Trait.GREENTHUMB))
			modifications.add(new ModificationData(TargetType.ALL, 0.7f, EssentialAttributes.MOTIVATION));
		else
			modifications.add(new ModificationData(TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
		return modifications;
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public void perform() {
		Garden room = (Garden)getCharacterLocation();
		efficiency+=(getCharacters().get(0).getIntelligence()/2)+getCharacters().get(0).getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE)/20;
		
		if(room.getPlant()==Plant.FLOWERS){efficiency*=120/100;}
		if(room.getPlant()==Plant.CATNIP){efficiency*=80/100;}
		if(room.getPlant()==Plant.WEED){efficiency*=50/100;}
		if(room.getPlant()==Plant.BERRIES){efficiency*=90/100;}
		if(room.getPlant()==Plant.CHERRIES){efficiency*=90/100;}
		if(room.getPlant()==Plant.ORANGES){efficiency*=80/100;}
		if(room.getPlant()==Plant.PEACHES){efficiency*=70/100;}
		if(room.getPlant()==Plant.SHROOMS){efficiency*=80/100;}
		if(room.getPlant()==Plant.VINES){efficiency*=60/100;}
		if(room.getGrowth()!=0){//grow plants
			room.setGrowth(room.getGrowth()+efficiency+1);
			room.setQuality(room.getQuality()+efficiency/2);
		}
		if(room.getGrowth()==0){//plant stuff
			List<SelectionData<Integer>> options = new ArrayList<SelectionData<Integer>>();
			options.add(new SelectionData<Integer>(0, TextUtil.t("gardening.plant.flowers")));
			options.add(new SelectionData<Integer>(1, TextUtil.t("gardening.plant.cherries")));
			options.add(new SelectionData<Integer>(2, TextUtil.t("gardening.plant.berries")));
			options.add(new SelectionData<Integer>(3, TextUtil.t("gardening.plant.catnip")));
			options.add(new SelectionData<Integer>(4, TextUtil.t("gardening.plant.oranges")));
			options.add(new SelectionData<Integer>(5, TextUtil.t("gardening.plant.shrooms")));
			options.add(new SelectionData<Integer>(6, TextUtil.t("gardening.plant.peaches")));
			options.add(new SelectionData<Integer>(7, TextUtil.t("gardening.plant.vines")));
			options.add(new SelectionData<Integer>(8, TextUtil.t("gardening.plant.weed")));
			
			SelectionData<Integer> selectedOption = new SelectionScreen<Integer>()
					.select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacters().get(0)), null,
							getCharacters().get(0).getBackground(), TextUtil.t("gardening.plant.select", getCharacters().get(0)));
			Integer selected = selectedOption.getSelectionObject();
			
			if(selected==0){room.setPlant(Plant.FLOWERS); room.setGrowth(10); room.setPlantName("Flowers");}
			if(selected==1){room.setPlant(Plant.CHERRIES); room.setGrowth(10); room.setPlantName("Cherries");}
			if(selected==2){room.setPlant(Plant.BERRIES); room.setGrowth(10); room.setPlantName("Berries");}
			if(selected==3){room.setPlant(Plant.CATNIP); room.setGrowth(10); room.setPlantName("Catnip");}
			if(selected==4){room.setPlant(Plant.ORANGES); room.setGrowth(10); room.setPlantName("Oranges");}
			if(selected==5){room.setPlant(Plant.SHROOMS); room.setGrowth(10); room.setPlantName("Shrooms");}
			if(selected==6){room.setPlant(Plant.PEACHES); room.setGrowth(10); room.setPlantName("Peaches");}
			if(selected==7){room.setPlant(Plant.VINES); room.setGrowth(10); room.setPlantName("Vines");}
			if(selected==8){room.setPlant(Plant.WEED); room.setGrowth(10); room.setPlantName("Weed");}
			System.out.println(room.getPlant());
		}
	}
	
	@Override
	public MessageData getBaseMessage() {
		Charakter character = getCharacters().get(0);
		
		ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);;
		String message = TextUtil.t("garden.basic", character);
		return new MessageData(message, image, null);
	}
	
	public int getEfficiency() {
		return efficiency;
	}
	
	public void setEfficiency(int efficiency) {
		this.efficiency = efficiency;
	}
}