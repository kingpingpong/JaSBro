package jasbro.util.itemEditor.equipmentEffectPanel;

import jasbro.game.events.business.CustomerType;
import jasbro.game.items.equipmentEffect.EquipmentChangeCustomerTypeSatisfaction;
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

public class EquipmentChangeCustomerTypeSatisfactionPanel extends JPanel {
	private EquipmentChangeCustomerTypeSatisfaction itemEffect;

	public EquipmentChangeCustomerTypeSatisfactionPanel(EquipmentEffect equipmentEffect) {
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
		this.itemEffect = (EquipmentChangeCustomerTypeSatisfaction)equipmentEffect;
		add(new JLabel(TextUtil.t("customertype")), "1, 2, left, center");
		final JComboBox<CustomerType> customerTypeComboBox = new JComboBox<CustomerType>();
		add(customerTypeComboBox, "2, 2, fill, top");
		for (CustomerType customerType : CustomerType.values()) {
			customerTypeComboBox.addItem(customerType);
		}
		customerTypeComboBox.setSelectedItem(itemEffect.getCustomerType());
		customerTypeComboBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				itemEffect.setCustomerType((CustomerType)customerTypeComboBox.getSelectedItem());
			}
		});
		
		add(new JLabel(TextUtil.t("ui.change")), "1, 3, left, center");
		final JSpinner spinner = new JSpinner();
		spinner.setValue(itemEffect.getAmount());
		add(spinner, "2, 3, fill, top");
		spinner.addChangeListener(new ChangeListener() {				
			@Override
			public void stateChanged(ChangeEvent e) {
				itemEffect.setAmount((int)spinner.getValue());
			}
		});
	}
}
