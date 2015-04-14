package jasbro.game.character.warnings;

import jasbro.gui.pictures.ImageData;

public enum Severity {
    ACTIVITY("images/icons/work.png"), 
    INFO("images/icons/info-pic.png"), 
    WARN("images/icons/warn.png"),
    DANGER("images/icons/Exclamation_Mark.png")
    ;
    
    private ImageData icon;
    
    private Severity(String image) {
        icon = new ImageData(image);
    }

    public ImageData getIcon() {
        return icon;
    }

}
