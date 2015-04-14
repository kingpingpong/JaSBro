package jasbro.util;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.housing.House;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.locations.DivLocations;
import jasbro.texts.TextUtil;

public class UserHelper {

    public void perform(AreaInterface area, HelpOption helpOption) {
        if (helpOption == HelpOption.REMOVEALL) {
            removeAll(area);
        } else if (helpOption == HelpOption.COPYMORNINGSHIFT) {
            copy(area, Time.MORNING);
        } else if (helpOption == HelpOption.COPYAFTERNOONSHIFT) {
            copy(area, Time.AFTERNOON);
        } else if (helpOption == HelpOption.COPYNIGHTSHIFT) {
            copy(area, Time.NIGHT);
        } else if (helpOption == HelpOption.REMOVEALLEVERYWHERE || helpOption == HelpOption.COPYMORNINGSHIFTEVERYWHERE
                || helpOption == HelpOption.COPYAFTERNOONSHIFTEVERYWHERE || helpOption == HelpOption.COPYNIGHTSHIFTEVERYWHERE) {
            for (House house : Jasbro.getInstance().getData().getHouses()) {
                if (helpOption == HelpOption.REMOVEALLEVERYWHERE) {
                    perform(house, HelpOption.REMOVEALL);
                } else if (helpOption == HelpOption.COPYMORNINGSHIFTEVERYWHERE) {
                    perform(house, HelpOption.COPYMORNINGSHIFT);
                } else if (helpOption == HelpOption.COPYAFTERNOONSHIFTEVERYWHERE) {
                    perform(house, HelpOption.COPYAFTERNOONSHIFT);
                } else if (helpOption == HelpOption.COPYNIGHTSHIFTEVERYWHERE) {
                    perform(house, HelpOption.COPYNIGHTSHIFT);
                }
            }
            AreaInterface location = new DivLocations();
            if (helpOption == HelpOption.REMOVEALLEVERYWHERE) {
                perform(location, HelpOption.REMOVEALL);
            } else if (helpOption == HelpOption.COPYMORNINGSHIFTEVERYWHERE) {
                perform(location, HelpOption.COPYMORNINGSHIFT);
            } else if (helpOption == HelpOption.COPYAFTERNOONSHIFTEVERYWHERE) {
                perform(location, HelpOption.COPYMORNINGSHIFT);
            } else if (helpOption == HelpOption.COPYNIGHTSHIFTEVERYWHERE) {
                perform(location, HelpOption.COPYNIGHTSHIFT);
            }
        }
    }

    public void copy(AreaInterface area, Time timeToCopy) {
        Time curtime = Jasbro.getInstance().getData().getTime();
        if (timeToCopy != curtime) {
            for (CharacterLocation location : area.getLocations()) {
                PlannedActivity curActivity = location.getCurrentUsage();
                PlannedActivity activityToCopy = location.getUsage(timeToCopy);

                curActivity.removeAllCharacters();
                for (Charakter character : activityToCopy.getCharacters()) {
                    curActivity.add(character);
                }
                curActivity.setType(activityToCopy.getType());
                curActivity.setSelectedOption(activityToCopy.getSelectedOption());
            }
        }
    }

    public void removeAll(AreaInterface area) {
        for (CharacterLocation location : area.getLocations()) {
            PlannedActivity curActivity = location.getCurrentUsage();
            curActivity.removeAllCharacters();
        }
    }

    public static enum HelpOption {
        COPYMORNINGSHIFT, COPYAFTERNOONSHIFT, COPYNIGHTSHIFT, REMOVEALL, COPYMORNINGSHIFTEVERYWHERE, COPYAFTERNOONSHIFTEVERYWHERE, COPYNIGHTSHIFTEVERYWHERE, REMOVEALLEVERYWHERE;

        public String getText() {
            return TextUtil.t(this.toString());
        }
    }
}
