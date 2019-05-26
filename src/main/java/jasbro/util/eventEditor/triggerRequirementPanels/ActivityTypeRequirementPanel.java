package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.character.activities.ActivityType;
import jasbro.game.world.customContent.requirements.ActivityRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ActivityTypeRequirementPanel extends JPanel {
	private ActivityRequirement triggerRequirement;
	private JComboBox<ActivityType> activityTypeComboBox;
	
	public ActivityTypeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (ActivityRequirement) triggerRequirementTmp;
		JLabel label_1 = new JLabel(TextUtil.t("eventEditor.activityType"));
		add(label_1, "1, 2, left, center");
		activityTypeComboBox = new JComboBox<ActivityType>();
		add(activityTypeComboBox, "2, 2, fill, top");
		activityTypeComboBox.addItem(null);
		for (ActivityType activityType : ActivityType.values()) {
			activityTypeComboBox.addItem(activityType);
		}
		activityTypeComboBox.setSelectedItem(triggerRequirement.getActivityType());
		activityTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerRequirement.setActivityType((ActivityType)activityTypeComboBox.getSelectedItem());
			}
		});
		
	}
	
	
}