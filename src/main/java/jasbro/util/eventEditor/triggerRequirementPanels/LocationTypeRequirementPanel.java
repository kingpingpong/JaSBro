package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.housing.RoomType;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.customContent.requirements.LocationTypeRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.locations.LocationType;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LocationTypeRequirementPanel extends JPanel {
    private LocationTypeRequirement triggerRequirement;
    private JComboBox<LocationTypeInterface> activityTypeComboBox;

    public LocationTypeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
        this.triggerRequirement = (LocationTypeRequirement) triggerRequirementTmp;
        JLabel label_1 = new JLabel(TextUtil.t("eventEditor.activityType"));
        add(label_1, "1, 2, left, center");
        activityTypeComboBox = new JComboBox<LocationTypeInterface>();
        add(activityTypeComboBox, "2, 2, fill, top");
        activityTypeComboBox.addItem(null);
        for (LocationTypeInterface locationType : LocationType.values()) {
            activityTypeComboBox.addItem(locationType);
        }
        for (RoomType locationType : RoomType.values()) {
            activityTypeComboBox.addItem(locationType);
        }
        activityTypeComboBox.setSelectedItem(triggerRequirement.getLocationType());
        activityTypeComboBox.addActionListener(new ActionListener() {          
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerRequirement.setLocationType((LocationTypeInterface)activityTypeComboBox.getSelectedItem());
            }
        });
        
    }
    

}
