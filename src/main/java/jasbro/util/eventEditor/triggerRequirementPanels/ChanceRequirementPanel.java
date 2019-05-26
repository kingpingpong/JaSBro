package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.ChanceRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChanceRequirementPanel extends JPanel {
	private ChanceRequirement triggerRequirement;
	
	public ChanceRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (ChanceRequirement)triggerRequirementTmp;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		
		final JSpinner spinner = new JSpinner();
		spinner.setValue(triggerRequirement.getChance());
		add(spinner);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				triggerRequirement.setChance((int)spinner.getValue());
			}
		});
	}
}