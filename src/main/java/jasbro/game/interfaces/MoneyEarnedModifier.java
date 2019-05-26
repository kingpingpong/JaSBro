package jasbro.game.interfaces;

import jasbro.game.character.Charakter;

public interface MoneyEarnedModifier {
	public float getMoneyModifier(float currentModifier, Charakter character);
}