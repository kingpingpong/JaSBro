package jasbro.game.items.equipmentEffect;

import jasbro.game.character.AttributeModifier;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MyEvent;
import jasbro.gui.pictures.ImageTag;

import java.io.Serializable;
import java.util.List;

public abstract class EquipmentEffect implements Serializable, AttributeModifier {
    public abstract EquipmentEffectType getType();
    public abstract double getValue();
    public abstract int getAmountEffects();

    public double getValueExponential() {
        return getValue() / 2000.0;
    }

    public String getName() {
        return getType().getText();
    }

    public String getDescription() {
        return "";
    }

    @Override
    public float getAttributeModifier(Attribute attribute) {
        return 0;
    }

    public void doAtEquip(Charakter character) {
    }

    public boolean canEquip(Charakter character) {
        return true;
    }

    public void doAtUnEquip(Charakter character) {
    }

    public void modifyImageTags(List<ImageTag> imageTags) {
    }

    public void modifyTraits(List<Trait> traits, Charakter character) {
    }

    public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
        return value;
    }

    public void handleEvent(MyEvent e, Charakter character) {
    }

}
