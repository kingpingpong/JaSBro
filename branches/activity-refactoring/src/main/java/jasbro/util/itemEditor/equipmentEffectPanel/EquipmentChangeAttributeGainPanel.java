package jasbro.util.itemEditor.equipmentEffectPanel;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.equipmentEffect.EquipmentChangeAttributeGain;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EquipmentChangeAttributeGainPanel extends JPanel {
	private EquipmentChangeAttributeGain itemEffect;

	public EquipmentChangeAttributeGainPanel(EquipmentEffect equipmentEffect) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		add(new JLabel(equipmentEffect.getName()), "1, 1, left, center");
		this.itemEffect = (EquipmentChangeAttributeGain)equipmentEffect;
		add(new JLabel(TextUtil.t("attribute")), "1, 2, left, center");
		final JComboBox<AttributeType> attributeTypeCombobox = new JComboBox<AttributeType>();
		add(attributeTypeCombobox, "2, 2, fill, top");
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
		attributeTypeCombobox.setSelectedItem(itemEffect.getAttributeType());
		attributeTypeCombobox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				EquipmentChangeAttributeGain effect = (EquipmentChangeAttributeGain) itemEffect;
				effect.setAttributeType((AttributeType)attributeTypeCombobox.getSelectedItem());
			}
		});
		add(new JLabel(TextUtil.t("ui.percent")), "1, 3, left, center");
		final JSpinner spinner = new JSpinner();
		spinner.setValue(itemEffect.getAmountPercent());
		add(spinner, "2, 3, fill, top");
		spinner.addChangeListener(new ChangeListener() {				
			@Override
			public void stateChanged(ChangeEvent e) {
				EquipmentChangeAttributeGain effect = (EquipmentChangeAttributeGain) itemEffect;
				effect.setAmountPercent((int)spinner.getValue());
			}
		});
	}
}
