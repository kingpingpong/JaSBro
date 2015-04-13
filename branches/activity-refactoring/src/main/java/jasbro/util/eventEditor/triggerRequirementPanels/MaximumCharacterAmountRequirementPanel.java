package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.MaximumCharacterAmountRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MaximumCharacterAmountRequirementPanel extends JPanel {
	private MaximumCharacterAmountRequirement triggerRequirement;

	public MaximumCharacterAmountRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (MaximumCharacterAmountRequirement)triggerRequirementTmp;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		        
		
        final JSpinner spinner = new JSpinner();
        spinner.setValue(triggerRequirement.getMaximum());
        add(spinner);
        spinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                triggerRequirement.setMaximum((int)spinner.getValue());
            }
        });
	}
}
