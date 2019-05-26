package jasbro.game.world.customContent.effects;

import java.util.ArrayList;
import java.util.List;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventChangeAttribute extends WorldEventEffect {
	private AttributeType attributeType = EssentialAttributes.ENERGY;
	private float value = 0;
	private String target = WorldEventVariables.character.toString();
	private boolean addToActivity = false; //Enable setting this in the editor
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		Charakter character = (Charakter)worldEvent.getAttribute(target);
		AttributeModification modification = new AttributeModification(value, attributeType, character);
		if (worldEvent.getActivity() != null && !worldEvent.getActivity().isAbort() && addToActivity) {
			worldEvent.getActivity().getAttributeModifications().add(modification);
		}
		else {
			modification.applyModification(worldEvent.getActivity());
			
			@SuppressWarnings("unchecked")
			List<AttributeModification> modifications = 
			(List<AttributeModification>) worldEvent.getAttribute(WorldEventVariables.attributemodifications);
			if (modifications == null) {
				modifications = new ArrayList<AttributeModification>();
				worldEvent.putAttribute(WorldEventVariables.attributemodifications, modifications);
			}
			modifications.add(modification);
		}
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.CHANGEATTRIBUTE;
	}
	
	public AttributeType getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	
}