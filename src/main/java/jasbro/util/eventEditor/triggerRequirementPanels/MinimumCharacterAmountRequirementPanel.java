package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.MinimumCharacterAmountRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MinimumCharacterAmountRequirementPanel extends JPanel {
	private MinimumCharacterAmountRequirement triggerRequirement;
	
	public MinimumCharacterAmountRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (MinimumCharacterAmountRequirement)triggerRequirementTmp;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		
		final JSpinner spinner = new JSpinner();
		spinner.setValue(triggerRequirement.getMinimum());
		add(spinner);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				triggerRequirement.setMinimum((int)spinner.getValue());
			}
		});
	}
}