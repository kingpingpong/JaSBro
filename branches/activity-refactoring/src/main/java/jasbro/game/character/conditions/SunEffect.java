package jasbro.game.character.conditions;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;

import java.util.List;

public class SunEffect extends Buff {
    private boolean sunburn = false;
    private int intensity;

    public SunEffect() {
        super("buff.lightTan", new ImageData(), 10);
    }

    @Override
    public void init() {
        Buff existingBuff = getExistingBuff();
        if (existingBuff != null) {
            ((SunEffect) existingBuff).increaseIntensity();
            getCharacter().getConditions().remove(this);
        }
        else {
            updateEffect();
        }
    }

    public void increaseIntensity() {
        int intensity = getIntensity() + 10;
        setIntensity(intensity);
        setRemainingTime(getRemainingTime()+10);
        updateEffect();
        if (sunburn) {
            getCharacter().getAttribute(EssentialAttributes.HEALTH).addToValue(-5);
        }
    }
    
    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTSHIFT) {
            setRemainingTime(getRemainingTime() - 1);
            setIntensity(getIntensity() - 1);
            updateEffect();
            if (getRemainingTime() <= 0) {
                getCharacter().getConditions().remove(this);
            }
        }
    }
    
    public void updateEffect() {
        if (!sunburn) {
            int intensity = getIntensity();
            if (intensity <= 10) {
                setNameKey("buff.lightTan");
                getAttributeModifiers().clear();
                addAttributeBuff(BaseAttributeTypes.CHARISMA, 3+getCharacter().getCharisma()/20);
            } else if (intensity <= 20) {
                setNameKey("buff.tan");
                getAttributeModifiers().clear();
                addAttributeBuff(BaseAttributeTypes.CHARISMA, 10+getCharacter().getCharisma()/8);
            } else {
                sunburn = true;
                setNameKey("buff.sunburn");
                getAttributeModifiers().clear();
                addAttributeBuff(BaseAttributeTypes.CHARISMA, -1-getCharacter().getCharisma()/3);
                setRemainingTime(15);
            }
        }
    }
    
    @Override
    public ImageData getIcon() {
        if (getNameKey().equals("buff.lightTan")) {
            return new ImageData("images/icons/light_tan.png");
        }
        else if (getNameKey().equals("buff.tan")) {
            return new ImageData("images/icons/tan.png");
        }
        else {
            return new ImageData("images/icons/sunburn.png");
        }
    }
    
    @Override
    public String getDescription() {
        if (!sunburn) {
            return super.getDescription();
        }
        else {
            Object arguments[] = {getRemainingTime()};
            return TextUtil.t("buff.sunburn.description", getCharacter(), arguments);
        }
    }

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

    public boolean isSunburn() {
        return sunburn;
    }
    
    @Override
    public void modifyImageTags(List<ImageTag> imageTags) {
        if (!sunburn) {
            imageTags.add(ImageTag.TANNED);
        }
    }
}
