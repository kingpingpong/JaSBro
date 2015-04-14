package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.events.business.SpawnData.CustomerData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Advertise extends RunningActivity {
    private MessageData message;
    private Charakter character;
    private int effectiveness = 1;

    @Override
    public void init() {
        character = getCharacter();
    }
    
    @Override
    public MessageData getBaseMessage() {
        if (message == null) {
            List<ImageTag> tags = character.getBaseTags();
            tags.add(0, ImageTag.CLOTHED);
            tags.add(1, ImageTag.CLEANED);
            Object arguments[] = {getCharacterLocation().getName()};
            message = new MessageData(TextUtil.t("advertise.basic", character, arguments), ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()), 
                    getCharacterLocation().getImage());
            if (!character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
                message.addToMessage(TextUtil.t("advertise.hintTraining", character));
            }
        }
        return message;
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        if (character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
            modifications.add(new ModificationData(TargetType.ALL, 1.1f, SpecializationAttribute.ADVERTISING));
        }
        else {
            modifications.add(new ModificationData(TargetType.ALL, 0.1f, SpecializationAttribute.ADVERTISING));
        }
        modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
        
        return modifications;
    }
    
    @Override
    public void perform() {
        List<House> listHouses = new ArrayList<House>();
        List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
        int amountHouses;
        for (House house : Jasbro.getInstance().getData().getHouses()) {
            for (Room room : house.getRooms()) {
                if (room.getAmountPeople() > 0 && room.getSelectedActivity().isCustomerDependent()) {
                    listHouses.add(house);
                    break;
                }
            }
        }
        amountHouses = listHouses.size();
        
        long skill = effectiveness;
        skill += character.getCharisma() / 10;
        skill += character.getFinalValue(SpecializationAttribute.ADVERTISING) / 10;
        skill += character.getFinalValue(SpecializationAttribute.STRIP) / 20;
        skill += character.getFinalValue(SpecializationAttribute.SEDUCTION) / 40;
        skill += character.getFinalValue(SpecializationAttribute.CATGIRL) / 40;
        
        long fame = character.getFame().getFame();        
        if (fame > 100) {
            long fameBonus = 0;
            int divider = 500;
            do {
                fame = fame / divider;
                fameBonus++;
                divider *= 4;
            }
            while (fame > 0);
            skill += fameBonus;
        }
                
        if (amountHouses > 0) {
            long increasePercent = skill / amountHouses;
            if (increasePercent == 0) {
                increasePercent = 1;
            }
            
            if (character.getSpecializations().contains(SpecializationType.CATGIRL) && character.getTraits().contains(Trait.CATCHY) && 
                    character.getFinalValue(SpecializationAttribute.CATGIRL) > 30) {
                int amountCustomers = character.getFinalValue(SpecializationAttribute.CATGIRL) / 30;
                if (amountCustomers / amountHouses > 0) {
                    int amountMinorNobles = Util.getInt(0, amountCustomers + 1);
                    int amountLords = amountCustomers - amountMinorNobles;

                    Object arguments[] = {amountMinorNobles, amountLords};
                    message.addToMessage(TextUtil.t("advertise.catgirl", character, arguments));
                    int finalAmountMinorNobles = amountMinorNobles / amountHouses;
                    int finalAmountLords = amountLords / amountHouses;
                    if (finalAmountMinorNobles == 0 && finalAmountLords == 0) {
                        finalAmountMinorNobles = 1;
                    }
                    bonusCustomers.add(new CustomerData(CustomerType.MINORNOBLE, finalAmountMinorNobles));
                    bonusCustomers.add(new CustomerData(CustomerType.LORD, finalAmountLords));
                }            
            }
            
            if (character.getSpecializations().contains(SpecializationType.DANCER) && character.getTraits().contains(Trait.SHOWINGTHEGOODS) && character.getFinalValue(SpecializationAttribute.STRIP) > 40) {
                message.addToMessage(TextUtil.t("advertise.dance", character));
            }
            
            if (character.getSpecializations().contains(SpecializationType.FIGHTER) && character.getTraits().contains(Trait.SHOWOFF) && 
                    character.getFinalValue(SpecializationAttribute.VETERAN) > 20) {
                int amountCustomers = character.getFinalValue(SpecializationAttribute.VETERAN) / 20;
                if (amountCustomers / amountHouses > 0) {
                    Object arguments[] = {amountCustomers};
                    message.addToMessage(TextUtil.t("advertise.fighter", character, arguments));
                    int finalAmount = amountCustomers / amountHouses;
                    bonusCustomers.add(new CustomerData(CustomerType.SOLDIER, finalAmount));
                }            
            }
            
            if (character.getType() == CharacterType.SLAVE && character.getSpecializations().contains(SpecializationType.WHORE) && character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && 
                    character.getFinalValue(SpecializationAttribute.SEDUCTION) > 15) {
                int amountCustomers = character.getFinalValue(SpecializationAttribute.SEDUCTION) / 15;
                if (amountCustomers / amountHouses > 0) {
                    int amountPeasants = Util.getInt(0, amountCustomers + 1);
                    int amountMerchants = amountCustomers - amountPeasants;

                    Object arguments[] = {amountPeasants, amountMerchants};
                    message.addToMessage(TextUtil.t("advertise.whore", character, arguments));
                    int finalAmountPeasants = amountPeasants / amountHouses;
                    int finalAmountMerchants = amountMerchants / amountHouses;
                    if (finalAmountPeasants == 0 && finalAmountMerchants == 0) {
                        finalAmountPeasants = 1;
                    }
                    bonusCustomers.add(new CustomerData(CustomerType.PEASANT, finalAmountPeasants));
                    bonusCustomers.add(new CustomerData(CustomerType.MERCHANT, finalAmountMerchants));
                }            
            }
            
            if (character.getSpecializations().contains(SpecializationType.BARTENDER) && character.getTraits().contains(Trait.HEYGUYSBOOSE) && 
                    character.getFinalValue(SpecializationAttribute.BARTENDING) > 10) {
                int amountCustomers = character.getFinalValue(SpecializationAttribute.BARTENDING) / 10;
                if (amountCustomers > 0) {
                    Object arguments[] = {amountCustomers};
                    message.addToMessage(TextUtil.t("advertise.bartend", character, arguments));
                    int finalAmount = amountCustomers / amountHouses;
                    if (finalAmount < 1) {
                        finalAmount = 1;
                    }
                    bonusCustomers.add(new CustomerData(CustomerType.BUM, finalAmount));
                }
            }
            
            Object arguments[] = {increasePercent};
            message.addToMessage("\n\n" + TextUtil.t("advertise.result", character, arguments));
            
            for (House house : listHouses) {
                SpawnData spawnData = house.getSpawnData();
                spawnData.addToModCustomerAmount(increasePercent / 100.0f);
                for (CustomerData bonusCustData : bonusCustomers) {
                    spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
                }
            }
        }
        else {
            List<House> houses = Jasbro.getInstance().getData().getHouses();
            long increaseFameBy = (skill * 50) / houses.size();
            for (House house : houses) {
                house.getFame().modifyFame(increaseFameBy);
            }
            message.addToMessage("\n\n" + TextUtil.t("advertise.increaseFame", character));
        }
        character.getFame().modifyFame(skill * 10);
    }

    public int getStartingEffectiveness() {
        return effectiveness;
    }

    public void setStartingEffectiveness(int effectiveness) {
        this.effectiveness = effectiveness;
    }
    
    

}
