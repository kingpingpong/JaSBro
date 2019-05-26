package jasbro.util.itemEditor.equipmentEffectPanel;

import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.equipmentEffect.EquipmentChangeCalculatedAttributeFixed;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EquipmentChangeCalculatedAttributeFixedPanel extends JPanel {
	private EquipmentChangeCalculatedAttributeFixed itemEffect;
	
	public EquipmentChangeCalculatedAttributeFixedPanel(EquipmentEffect equipmentEffect) {
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
		this.itemEffect = (EquipmentChangeCalculatedAttributeFixed)equipmentEffect;
		add(new JLabel(TextUtil.t("attribute")), "1, 2, left, center");
		final JComboBox<AttributeType> attributeTypeCombobox = new JComboBox<AttributeType>();
		add(attributeTypeCombobox, "2, 2, fill, top");
		for (AttributeType attributeType : CalculatedAttribute.values()) {
			if (attributeType != CalculatedAttribute.SKILLPOINTS) {
				attributeTypeCombobox.addItem(attributeType);
			}
		}
		attributeTypeCombobox.setSelectedItem(itemEffect.getAttributeType());
		attributeTypeCombobox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EquipmentChangeCalculatedAttributeFixed effect = (EquipmentChangeCalculatedAttributeFixed) itemEffect;
				effect.setAttributeType((CalculatedAttribute)attributeTypeCombobox.getSelectedItem());
			}
		});
		add(new JLabel(TextUtil.t("ui.amount")), "1, 3, left, center");
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Double(0), new Double(-10000), new Double(10000), new Double(0.1f)));
		spinner.setValue(itemEffect.getAmount());
		add(spinner, "2, 3, fill, top");
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				EquipmentChangeCalculatedAttributeFixed effect = (EquipmentChangeCalculatedAttributeFixed) itemEffect;
				effect.setAmount((Double)spinner.getValue());
			}
		});
	}
}