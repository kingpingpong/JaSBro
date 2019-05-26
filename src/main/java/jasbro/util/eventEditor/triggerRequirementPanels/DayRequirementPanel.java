package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.DayRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement.Comparison;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DayRequirementPanel extends JPanel {
	private DayRequirement triggerRequirement;
	
	public DayRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (DayRequirement)triggerRequirementTmp;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		final JComboBox<Comparison> comboBox = new JComboBox<Comparison>();
		add(comboBox);
		for (Comparison dayComparison : Comparison.values()) {
			comboBox.addItem(dayComparison);
		}
		comboBox.setSelectedItem(triggerRequirement.getDayComparison());
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerRequirement.setDayComparison((Comparison)comboBox.getSelectedItem());
			}
		});
		
		final JSpinner spinner = new JSpinner();
		spinner.setValue(triggerRequirement.getDay());
		add(spinner);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				triggerRequirement.setDay((int)spinner.getValue());
			}
		});
	}
}