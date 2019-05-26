package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.CharacterIdRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterIdRequirementPanel extends JPanel {
	private CharacterIdRequirement triggerRequirement;
	
	public CharacterIdRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (CharacterIdRequirement)triggerRequirementTmp;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(150dlu;default):grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		final JTextField textField = new JTextField();
		add(textField, "1, 1, fill, fill");
		textField.setText(triggerRequirement.getCharacterId());
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				triggerRequirement.setCharacterId(textField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				triggerRequirement.setCharacterId(textField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				triggerRequirement.setCharacterId(textField.getText());
			}
		});
	}
}