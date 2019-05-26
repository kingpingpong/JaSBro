package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventSaveVariable;
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

public class EventEffectSaveVariablePanel extends JPanel {
	
	private WorldEventSaveVariable worldEventEffect;
	private JTextField textField;
	private JTextField textField_1;
	
	public EventEffectSaveVariablePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		this.worldEventEffect = (WorldEventSaveVariable)worldEventEffectTmp;
		
		JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.source"));
		add(lblNewLabel, "1, 1, right, fill");
		
		textField = new JTextField();
		add(textField, "2, 1, fill, default");
		textField.setText(worldEventEffect.getSource());
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				worldEventEffect.setSource(textField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				worldEventEffect.setSource(textField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				worldEventEffect.setSource(textField.getText());
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel(TextUtil.t("eventEditor.target"));
		add(lblNewLabel_1, "3, 1, right, default");
		
		textField_1 = new JTextField();
		add(textField_1, "4, 1, fill, default");
		textField_1.setText(worldEventEffect.getTarget());
		textField_1.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				worldEventEffect.setTarget(textField_1.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				worldEventEffect.setTarget(textField_1.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				worldEventEffect.setTarget(textField_1.getText());
			}
		});
		
	}
	
}