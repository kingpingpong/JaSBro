package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.FameRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FameRequirementPanel extends JPanel {
	private FameRequirement triggerRequirement;
	
	public FameRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (FameRequirement)triggerRequirementTmp;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(0L, 0L,
				Long.MAX_VALUE, 1L));
		spinner.setValue(triggerRequirement.getFameRequired());
		add(spinner);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double tmp = (Double)spinner.getValue();
				triggerRequirement.setFameRequired((long) tmp);
			}
		});
	}
}