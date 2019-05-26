package jasbro.util.eventEditor.triggerRequirementPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomInfoUtil;
import jasbro.game.housing.RoomUnlock;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.UnlockRequirement;

public class UnlockRequirementPanel extends JPanel {
	private UnlockRequirement triggerRequirement;
	private JComboBox<UnlockObject> unlockComboBox;
	
	public UnlockRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (UnlockRequirement) triggerRequirementTmp;
		unlockComboBox = new JComboBox<UnlockObject>();
		add(unlockComboBox);
		unlockComboBox.addItem(null);
		for (HouseType houseType : HouseType.values()) {
			unlockComboBox.addItem(houseType);
		}
		for(final RoomUnlock roomUnlock : RoomInfoUtil.getRoomUnlocks()) {
			unlockComboBox.addItem(roomUnlock);
		}
		for (SpecializationType specializationType : SpecializationType.values()) {
			if (specializationType != SpecializationType.TRAINER && 
					specializationType != SpecializationType.SLAVE &&
					specializationType != SpecializationType.UNDERAGE &&
					specializationType != SpecializationType.SEX &&
					specializationType.getAssociatedSkillTree() != null) {
				unlockComboBox.addItem(specializationType);
			}
		}
		
		if (triggerRequirement.getUnlockObject() != null) {
			unlockComboBox.setSelectedItem(triggerRequirement.getUnlockObject());
		}
		unlockComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerRequirement.setUnlockObject((UnlockObject)unlockComboBox.getSelectedItem());
			}
		});
		
	}
	
	
}