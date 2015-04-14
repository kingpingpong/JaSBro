package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.world.customContent.requirements.SpecializationRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SpecializationRequirementPanel extends JPanel {
	private SpecializationRequirement triggerRequirement;

	public SpecializationRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default")},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		this.triggerRequirement = (SpecializationRequirement)triggerRequirementTmp;
		final JComboBox<SpecializationType> traitCombobox = new JComboBox<SpecializationType>();
		add(traitCombobox, "1, 1");
		for (SpecializationType trait : SpecializationType.values()) {
			traitCombobox.addItem(trait);
		}
		traitCombobox.setSelectedItem(triggerRequirement.getSpecialization());
		traitCombobox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
			    triggerRequirement.setSpecialization((SpecializationType)traitCombobox.getSelectedItem());
			}
		});
	}
}
