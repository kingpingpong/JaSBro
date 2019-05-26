package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.world.Time;

public class ThiefPerks {
	
	public static class DoubleVie extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue,
				Charakter character) {
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
				if (calculatedAttribute == CalculatedAttribute.STEALAMOUNTMODIFIER) {
					return currentValue + 30;
				}
				else if (calculatedAttribute == CalculatedAttribute.STEALITEMCHANCE) {
					return currentValue + 30;
				}
				else if (calculatedAttribute == CalculatedAttribute.STEALCHANCE) {
					return currentValue + 30;
				}
			}
			return currentValue;
		}
	}
	public static class NightShade extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue,
				Charakter character) {
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {

				if (calculatedAttribute == CalculatedAttribute.STEALITEMCHANCE) {
					return currentValue + 10;
				}
				else if (calculatedAttribute == CalculatedAttribute.STEALAMOUNTMODIFIER) {
					return currentValue + 10;
				}
				else if (calculatedAttribute == CalculatedAttribute.STEALCHANCE) {
					return currentValue + 30;
				}
			}
			return currentValue;

		}
	}
	public static class ShadowBody extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue,
				Charakter character) {
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
				if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
					return currentValue + 3;
				}
				else if (calculatedAttribute == CalculatedAttribute.DODGE) {
					return currentValue + 3;
				}
				else if (calculatedAttribute == CalculatedAttribute.HIT) {
					return currentValue + 3;
				}
				else if (calculatedAttribute == CalculatedAttribute.CRITCHANCE) {
					return currentValue + 3;
				}
				else if (calculatedAttribute == CalculatedAttribute.CRITDAMAGEAMOUNT) {
					return currentValue + 1;
				}

			}
			return currentValue;

		}
	}

}
