package jasbro.game.items;

import jasbro.texts.TextUtil;

public enum ItemLocation {
    SHOP, CLOTHINGSTORE, ADVENTURERSSHOP, APOTHECARY, ADULTSTORE, TRAVELLINGMERCHANT, BLACKMARKET, 
    LORD, MINORNOBLE, BUSINESSMAN, MERCHANT, SOLDIER, PEASANT, BUM, CELEBRITY;

    public String getText() {
        String text = TextUtil.tNoCheck(this.toString());
        if (text == null) {
            text = this.toString();
            text = text.charAt(0) + text.substring(1).toLowerCase();
        }
        return text;
    }
}
