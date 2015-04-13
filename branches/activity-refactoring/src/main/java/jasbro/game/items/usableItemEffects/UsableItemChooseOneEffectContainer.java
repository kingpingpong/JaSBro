package jasbro.game.items.usableItemEffects;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.items.Item;

import java.util.List;

public class UsableItemChooseOneEffectContainer extends UsableItemEffectContainerImpl {

	@Override
	public void apply(Charakter character, Item item) {
		List<UsableItemEffect> effects = getSubEffects();
		int amountChance = 0;
		if (effects.size() > 0) {
			for (UsableItemEffect itemEffect : effects) {
				if (itemEffect instanceof UsableItemEffectChance) {
					amountChance++;
				}
			} 
			if (amountChance != effects.size()) {
				//Go random
				effects.get(Util.getInt(0, effects.size())).apply(character, item);
			}
			else {
				int sumChances = 0;
				for (UsableItemEffect itemEffect : effects) {
					sumChances += ((UsableItemEffectChance) itemEffect).getChance();
				}
				int selected = Util.getInt(0, sumChances);
				sumChances = 0;
				for (UsableItemEffect itemEffect : effects) {
					sumChances += ((UsableItemEffectChance) itemEffect).getChance();
					if (sumChances > selected) {
						((UsableItemEffectChance)itemEffect).applyOverride(character, item);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.CHOOSEONEFFECT;
	}
	
	@Override
	public String getName() {
		return "Choose one subeffect container";
	}
}
