package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventRemoveCharacter;
import jasbro.texts.TextUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectRemoveCharacterPanel extends JPanel {
	private WorldEventRemoveCharacter worldEventEffect;
	private JTextField textField;
	
	public EventEffectRemoveCharacterPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow")
		},
		new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		this.worldEventEffect = (WorldEventRemoveCharacter)worldEventEffectTmp;
		
		JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.target"));
		add(lblNewLabel, "1, 1, right, fill");
		
		textField = new JTextField();
		add(textField, "2, 1, fill, default");
		textField.setText(worldEventEffect.getTarget());
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				worldEventEffect.setTarget(textField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				worldEventEffect.setTarget(textField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				worldEventEffect.setTarget(textField.getText());
			}
		});
		
		
	}
}