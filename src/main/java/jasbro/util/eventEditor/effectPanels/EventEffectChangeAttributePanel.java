package jasbro.util.eventEditor.effectPanels;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventChangeAttribute;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
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

public class EventEffectChangeAttributePanel extends JPanel {
	private WorldEventChangeAttribute worldEventEffect;
	private JTextField textField;
	
	public EventEffectChangeAttributePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		this.worldEventEffect = (WorldEventChangeAttribute)worldEventEffectTmp;
		
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
		
		final JComboBox<AttributeType> attributeTypeCombobox = new JComboBox<AttributeType>();
		add(attributeTypeCombobox, "3, 1");
		for (AttributeType attributeType : EssentialAttributes.values()) {
			attributeTypeCombobox.addItem(attributeType);
		}
		for (AttributeType attributeType : BaseAttributeTypes.values()) {
			attributeTypeCombobox.addItem(attributeType);
		}
		for (AttributeType attributeType : Sextype.values()) {
			attributeTypeCombobox.addItem(attributeType);
		}
		for (AttributeType attributeType : SpecializationAttribute.values()) {
			attributeTypeCombobox.addItem(attributeType);
		}
		attributeTypeCombobox.setSelectedItem(worldEventEffect.getAttributeType());
		attributeTypeCombobox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worldEventEffect.setAttributeType((AttributeType)attributeTypeCombobox.getSelectedItem());
			}
		});
		
		
		
		final JSpinner spinner = new JSpinner(
				new SpinnerNumberModel(worldEventEffect.getValue(), -20000f, Float.MAX_VALUE, 0.01f));
		add(spinner, "4, 1");
		spinner.addChangeListener(new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = (Double)spinner.getValue();
				worldEventEffect.setValue((float)value);
			}
		});
		
	}
}