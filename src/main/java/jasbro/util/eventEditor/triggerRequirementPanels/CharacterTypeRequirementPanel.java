package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.character.CharacterType;
import jasbro.game.world.customContent.requirements.CharacterTypeRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterTypeRequirementPanel extends JPanel {
	private CharacterTypeRequirement triggerRequirement;

	public CharacterTypeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default")},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		this.triggerRequirement = (CharacterTypeRequirement)triggerRequirementTmp;
		final JComboBox<CharacterType> traitCombobox = new JComboBox<CharacterType>();
		add(traitCombobox, "1, 1");
		for (CharacterType trait : CharacterType.values()) {
			traitCombobox.addItem(trait);
		}
		traitCombobox.setSelectedItem(triggerRequirement.getCharacterType());
		traitCombobox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
			    triggerRequirement.setCharacterType((CharacterType)traitCombobox.getSelectedItem());
			}
		});
	}
}
