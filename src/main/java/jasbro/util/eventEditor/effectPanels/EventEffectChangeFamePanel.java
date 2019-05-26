package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventChangeFame;
import jasbro.texts.TextUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectChangeFamePanel extends JPanel {
	private WorldEventChangeFame worldEventEffect;
	private JTextField textField;
	
	public EventEffectChangeFamePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		this.worldEventEffect = (WorldEventChangeFame)worldEventEffectTmp;
		
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
		
		
		
		
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
		spinner.setValue(worldEventEffect.getValue());
		add(spinner, "4, 1");
		spinner.addChangeListener(new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				worldEventEffect.setValue((int)spinner.getValue());
			}
		});
		
	}
}